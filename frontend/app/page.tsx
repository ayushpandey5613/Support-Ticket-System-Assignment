"use client";

import { ErrorAlert } from "@/components/ErrorAlert";
import { listTickets } from "@/lib/api";
import { ApiError, Ticket } from "@/lib/types";
import Link from "next/link";
import { useCallback, useEffect, useState } from "react";

const PAGE_SIZE = 20;

function formatDate(iso: string): string {
  try {
    return new Date(iso).toLocaleString();
  } catch {
    return iso;
  }
}

export default function TicketListPage() {
  const [tickets, setTickets] = useState<Ticket[]>([]);
  const [page, setPage] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<ApiError | Error | null>(null);

  const load = useCallback(async (pageIndex: number) => {
    setLoading(true);
    setError(null);
    try {
      const data = await listTickets(pageIndex, PAGE_SIZE);
      setTickets(data.content);
      setPage(data.page);
      setTotalElements(data.totalElements);
    } catch (e) {
      setTickets([]);
      setError(e instanceof Error ? e : new Error("Failed to load tickets"));
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    load(page);
  }, [page, load]);

  const totalPages = Math.max(1, Math.ceil(totalElements / PAGE_SIZE));
  const canPrev = page > 0;
  const canNext = page + 1 < totalPages;

  return (
    <div>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "1rem" }}>
        <h2 style={{ margin: 0 }}>Tickets</h2>
        <Link href="/tickets/new" className="btn btn-secondary">
          Create ticket
        </Link>
      </div>

      <ErrorAlert error={error} title="Could not load tickets" />

      <div className="card">
        {loading ? (
          <p className="muted">Loading tickets…</p>
        ) : tickets.length === 0 ? (
          <p className="muted">No tickets yet. Create your first ticket.</p>
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
                  <td>{t.title}</td>
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
