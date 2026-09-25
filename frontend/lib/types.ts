export type Priority = "LOW" | "MEDIUM" | "HIGH" | "URGENT";
export type TicketStatus =
  | "OPEN"
  | "IN_PROGRESS"
  | "RESOLVED"
  | "CLOSED"
  | "CANCELLED";

export interface Ticket {
  id: string;
  title: string;
  description: string;
  priority: Priority;
  status: TicketStatus;
  assignee: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface TicketPage {
  content: Ticket[];
  page: number;
  size: number;
  totalElements: number;
}

export interface FieldError {
  field: string;
  message: string;
}

export interface ApiErrorBody {
  status: number;
  code: string;
  message: string;
  fieldErrors?: FieldError[];
}

export class ApiError extends Error {
  readonly status: number;
  readonly code: string;
  readonly fieldErrors: FieldError[];

  constructor(body: ApiErrorBody) {
    super(body.message || "Request failed");
    this.name = "ApiError";
    this.status = body.status;
    this.code = body.code;
    this.fieldErrors = body.fieldErrors ?? [];
  }
}

export interface CreateTicketInput {
  title: string;
  description: string;
  priority?: Priority;
  assignee?: string;
}
