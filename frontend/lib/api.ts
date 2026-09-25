import {
  ApiError,
  ApiErrorBody,
  CreateCommentInput,
  CreateTicketInput,
  Comment,
  PatchTicketInput,
  Ticket,
  TicketDetail,
  TicketPage,
  TicketStatus,
} from "./types";

export function getApiBaseUrl(): string {
  return process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080/api/v1";
}

async function parseError(response: Response): Promise<ApiError> {
  let body: ApiErrorBody;
  try {
    body = (await response.json()) as ApiErrorBody;
  } catch {
    body = {
      status: response.status,
      code: "UNKNOWN_ERROR",
      message: response.statusText || "Request failed",
    };
  }
  if (!body.status) {
    body.status = response.status;
  }
  return new ApiError(body);
}

const REQUEST_TIMEOUT_MS = 15000;

export async function apiFetch<T>(
  path: string,
  options?: RequestInit
): Promise<T> {
  const url = `${getApiBaseUrl()}${path}`;
  const controller = new AbortController();
  const timeout = setTimeout(() => controller.abort(), REQUEST_TIMEOUT_MS);
  let response: Response;
  try {
    response = await fetch(url, {
      ...options,
      signal: controller.signal,
      headers: {
        "Content-Type": "application/json",
        ...(options?.headers ?? {}),
      },
    });
  } catch (e) {
    if (e instanceof Error && e.name === "AbortError") {
      throw new Error(
        "API request timed out. Is the backend running on port 8080? (mvn spring-boot:run in backend/)"
      );
    }
    if (e instanceof TypeError) {
      throw new Error(
        "Cannot reach the API. Start the backend (mvn spring-boot:run in backend/) and restart it after CORS changes. Check NEXT_PUBLIC_API_URL points to http://localhost:8080/api/v1."
      );
    }
    throw e;
  } finally {
    clearTimeout(timeout);
  }
  if (!response.ok) {
    throw await parseError(response);
  }
  if (response.status === 204) {
    return undefined as T;
  }
  return (await response.json()) as T;
}

export interface ListTicketsParams {
  page?: number;
  size?: number;
  q?: string;
  status?: TicketStatus;
}

export async function listTickets(
  params: ListTicketsParams = {}
): Promise<TicketPage> {
  const { page = 0, size = 20, q, status } = params;
  const search = new URLSearchParams({
    page: String(page),
    size: String(size),
  });
  if (q?.trim()) {
    search.set("q", q.trim());
  }
  if (status) {
    search.set("status", status);
  }
  return apiFetch<TicketPage>(`/tickets?${search}`);
}

export async function getTicket(id: string): Promise<TicketDetail> {
  return apiFetch<TicketDetail>(`/tickets/${id}`);
}

export async function createTicket(input: CreateTicketInput): Promise<Ticket> {
  return apiFetch<Ticket>("/tickets", {
    method: "POST",
    body: JSON.stringify(input),
  });
}

export async function patchTicket(
  id: string,
  input: PatchTicketInput
): Promise<Ticket> {
  return apiFetch<Ticket>(`/tickets/${id}`, {
    method: "PATCH",
    body: JSON.stringify(input),
  });
}

export async function addComment(
  ticketId: string,
  input: CreateCommentInput
): Promise<Comment> {
  return apiFetch<Comment>(`/tickets/${ticketId}/comments`, {
    method: "POST",
    body: JSON.stringify(input),
  });
}

export async function transitionTicketStatus(
  id: string,
  status: TicketStatus
): Promise<Ticket> {
  return apiFetch<Ticket>(`/tickets/${id}/status`, {
    method: "POST",
    body: JSON.stringify({ status }),
  });
}
