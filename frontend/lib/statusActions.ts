import { TicketStatus } from "./types";

export interface StatusAction {
  targetStatus: TicketStatus;
  label: string;
  secondary?: boolean;
}

const STATUS_ACTIONS: Record<TicketStatus, StatusAction[]> = {
  OPEN: [
    { targetStatus: "IN_PROGRESS", label: "Start progress" },
    { targetStatus: "CANCELLED", label: "Cancel ticket", secondary: true },
  ],
  IN_PROGRESS: [
    { targetStatus: "RESOLVED", label: "Mark resolved" },
    { targetStatus: "CANCELLED", label: "Cancel ticket", secondary: true },
  ],
  RESOLVED: [{ targetStatus: "CLOSED", label: "Close ticket" }],
  CLOSED: [],
  CANCELLED: [],
};

export function getStatusActions(status: TicketStatus): StatusAction[] {
  return STATUS_ACTIONS[status];
}
