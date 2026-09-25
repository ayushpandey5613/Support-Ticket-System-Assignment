"use client";

import { ErrorAlert } from "@/components/ErrorAlert";
import { listTickets } from "@/lib/api";
import { formatDate } from "@/lib/format";
import { ApiError, TICKET_STATUSES, Ticket, TicketStatus } from "@/lib/types";
import Link from "next/link";
import { useCallback, useEffect, useState } from "react";

const PAGE_SIZE = 20;
const SEARCH_DEBOUNCE_MS = 400;

export default function TicketListPage() {
  const [tickets, setTickets] = useState<Ticket[]>([]);
  const [page, setPage] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<ApiError | Error | null>(null);
  const [searchInput, setSearchInput] = useState("");
  const [debouncedQ, setDebouncedQ] = useState("");
  const [statusFilter, setStatusFilter] = useState<TicketStatus | "">("");

  useEffect(() => {
    const timer = setTimeout(() => setDebouncedQ(searchInput), SEARCH_DEBOUNCE_MS);
    return () => clearTimeout(timer);
  }, [searchInput]);

  useEffect(() => {
    setPage(0);
  }, [debouncedQ, statusFilter]);

  const load = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await listTickets({
        page,
        size: PAGE_SIZE,
        q: debouncedQ || undefined,
        status: statusFilter || undefined,
      });
      setTickets(data.content);
      setPage(data.page);
      setTotalElements(data.totalElements);
    } catch (e) {
      setTickets([]);
      setError(e instanceof Error ? e : new Error("Failed to load tickets"));
    } finally {
      setLoading(false);
    }
  }, [page, debouncedQ, statusFilter]);

  useEffect(() => {
    load();
  }, [load]);

  const totalPages = Math.max(1, Math.ceil(totalElements / PAGE_SIZE));
  const canPrev = page > 0;
  const canNext = page + 1 < totalPages;

  return (
    <div>
      <div className="list-header">
        <h2>Tickets</h2>
        <Link href="/tickets/new" className="btn btn-secondary">
          Create ticket
        </Link>
      </div>

      <div className="filters card">
        <div className="form-field" style={{ marginBottom: 0, flex: 1 }}>
          <label htmlFor="search">Search</label>
          <input
            id="search"
            type="search"
            placeholder="Title or description…"
            value={searchInput}
            onChange={(e) => setSearchInput(e.target.value)}
          />
        </div>
        <div className="form-field" style={{ marginBottom: 0, minWidth: "10rem" }}>
          <label htmlFor="status">Status</label>
          <select
            id="status"
            value={statusFilter}
            onChange={(e) =>
              setStatusFilter(e.target.value as TicketStatus | "")
            }
          >
            <option value="">All</option>
            {TICKET_STATUSES.map((s) => (
              <option key={s} value={s}>{s}</option>
            ))}
          </select>
        </div>
      </div>

      <ErrorAlert error={error} title="Could not load tickets" />

      <div className="card">
        {loading ? (
          <p className="muted">Loading tickets…</p>
        ) : tickets.length === 0 ? (
          <p className="muted">No tickets match your filters.</p>
        ) : (
          <table>
            <thead>
              <tr>
                <th>Title</th>
                <th>Status</th>
                <th>Priority</th>
                <th>Assignee</th>
                <th>Updated</th>
              </tr>
            </thead>
            <tbody>
              {tickets.map((t) => (
                <tr key={t.id}>
                  <td>
                    <Link href={`/tickets/${t.id}`}>{t.title}</Link>
                  </td>
                  <td><span className="badge">{t.status}</span></td>
                  <td>{t.priority}</td>
                  <td>{t.assignee ?? "—"}</td>
                  <td className="muted">{formatDate(t.updatedAt)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}

        {!loading && totalElements > 0 && (
          <div className="pagination">
            <button
              type="button"
              className="btn btn-secondary"
              disabled={!canPrev}
              onClick={() => setPage((p) => Math.max(0, p - 1))}
            >
              Previous
            </button>
            <span className="muted">
              Page {page + 1} of {totalPages} ({totalElements} total)
            </span>
            <button
              type="button"
              className="btn btn-secondary"
              disabled={!canNext}
              onClick={() => setPage((p) => p + 1)}
            >
              Next
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
