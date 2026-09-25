import {
  ApiError,
  ApiErrorBody,
  CreateTicketInput,
  Ticket,
  TicketPage,
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

export async function listTickets(page = 0, size = 20): Promise<TicketPage> {
  const params = new URLSearchParams({
    page: String(page),
    size: String(size),
  });
  return apiFetch<TicketPage>(`/tickets?${params}`);
}

export async function createTicket(input: CreateTicketInput): Promise<Ticket> {
  return apiFetch<Ticket>("/tickets", {
    method: "POST",
    body: JSON.stringify(input),
  });
}
