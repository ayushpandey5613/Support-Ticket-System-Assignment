"use client";

import { ErrorAlert } from "@/components/ErrorAlert";
import {
  addComment,
  getTicket,
  patchTicket,
  transitionTicketStatus,
} from "@/lib/api";
import { formatDate } from "@/lib/format";
import { getStatusActions } from "@/lib/statusActions";
import { ApiError, Priority, TicketDetail, TicketStatus } from "@/lib/types";
import Link from "next/link";
import { useParams } from "next/navigation";
import { FormEvent, useCallback, useEffect, useState } from "react";

const PRIORITIES: Priority[] = ["LOW", "MEDIUM", "HIGH", "URGENT"];

export default function TicketDetailPage() {
  const params = useParams();
  const id = params.id as string;

  const [ticket, setTicket] = useState<TicketDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<ApiError | Error | null>(null);

  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [priority, setPriority] = useState<Priority>("MEDIUM");
  const [assignee, setAssignee] = useState("");
  const [saving, setSaving] = useState(false);
  const [saveError, setSaveError] = useState<ApiError | Error | null>(null);
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});

  const [commentAuthor, setCommentAuthor] = useState("");
  const [commentBody, setCommentBody] = useState("");
  const [commentSubmitting, setCommentSubmitting] = useState(false);
  const [commentError, setCommentError] = useState<ApiError | Error | null>(null);

  const [statusBusy, setStatusBusy] = useState<TicketStatus | null>(null);
  const [statusError, setStatusError] = useState<ApiError | Error | null>(null);

  const load = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await getTicket(id);
      setTicket(data);
      setTitle(data.title);
      setDescription(data.description);
      setPriority(data.priority);
      setAssignee(data.assignee ?? "");
    } catch (e) {
      setTicket(null);
      setError(e instanceof Error ? e : new Error("Failed to load ticket"));
    } finally {
      setLoading(false);
    }
  }, [id]);

  useEffect(() => {
    load();
  }, [load]);

  function mapFieldErrors(apiError: ApiError): Record<string, string> {
    const map: Record<string, string> = {};
    for (const fe of apiError.fieldErrors) {
      map[fe.field] = fe.message;
    }
    return map;
  }

  async function onSave(e: FormEvent) {
    e.preventDefault();
    setSaving(true);
    setSaveError(null);
    setFieldErrors({});
    try {
      const updated = await patchTicket(id, {
        title,
        description,
        priority,
        assignee: assignee.trim() ? assignee.trim() : null,
      });
      setTicket((prev) =>
        prev ? { ...prev, ...updated, comments: prev.comments } : prev
      );
    } catch (err) {
      if (err instanceof ApiError) {
        setSaveError(err);
        setFieldErrors(mapFieldErrors(err));
      } else {
        setSaveError(err instanceof Error ? err : new Error("Save failed"));
      }
    } finally {
      setSaving(false);
    }
  }

  async function onAddComment(e: FormEvent) {
    e.preventDefault();
    setCommentSubmitting(true);
    setCommentError(null);
    try {
      await addComment(id, {
        author: commentAuthor.trim(),
        body: commentBody.trim(),
      });
      setCommentBody("");
      await load();
    } catch (err) {
      if (err instanceof ApiError) {
        setCommentError(err);
      } else {
        setCommentError(err instanceof Error ? err : new Error("Comment failed"));
      }
    } finally {
      setCommentSubmitting(false);
    }
  }

  async function onStatusAction(target: TicketStatus) {
    setStatusBusy(target);
    setStatusError(null);
    try {
      const updated = await transitionTicketStatus(id, target);
      setTicket((prev) =>
        prev ? { ...prev, ...updated, comments: prev.comments } : prev
      );
    } catch (err) {
      if (err instanceof ApiError) {
        setStatusError(err);
      } else {
        setStatusError(
          err instanceof Error ? err : new Error("Status change failed")
        );
      }
    } finally {
      setStatusBusy(null);
    }
  }

  if (loading) {
    return <p className="muted">Loading ticket…</p>;
  }

  if (!ticket) {
    return (
      <div>
        <ErrorAlert error={error} title="Ticket not found" />
        <Link href="/">← Back to list</Link>
      </div>
    );
  }

  const statusActions = getStatusActions(ticket.status);

  return (
    <div>
      <p><Link href="/">← Back to list</Link></p>
      <div className="detail-meta muted">
        Status: <span className="badge">{ticket.status}</span>
        · Updated {formatDate(ticket.updatedAt)}
      </div>

      {statusActions.length > 0 && (
        <section className="status-actions card" aria-label="Status actions">
          <h3 className="status-actions-title">Change status</h3>
          <ErrorAlert
            error={statusError}
            title="Could not update status"
          />
          <div className="status-actions-buttons">
            {statusActions.map((action) => (
              <button
                key={action.targetStatus}
                type="button"
                className={
                  action.secondary ? "btn btn-secondary" : "btn"
                }
                disabled={statusBusy !== null}
                onClick={() => onStatusAction(action.targetStatus)}
              >
                {statusBusy === action.targetStatus
                  ? "Updating…"
                  : action.label}
              </button>
            ))}
          </div>
        </section>
      )}

      <h2>Edit ticket</h2>
      <ErrorAlert error={saveError} title="Could not save changes" />

      <form className="card" onSubmit={onSave}>
        <div className="form-field">
          <label htmlFor="title">Title</label>
          <input
            id="title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
            maxLength={200}
          />
          {fieldErrors.title && (
            <div className="inline-error">{fieldErrors.title}</div>
          )}
        </div>
        <div className="form-field">
          <label htmlFor="description">Description</label>
          <textarea
            id="description"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            required
            maxLength={5000}
          />
          {fieldErrors.description && (
            <div className="inline-error">{fieldErrors.description}</div>
          )}
        </div>
        <div className="form-field">
          <label htmlFor="priority">Priority</label>
          <select
            id="priority"
            value={priority}
            onChange={(e) => setPriority(e.target.value as Priority)}
          >
            {PRIORITIES.map((p) => (
              <option key={p} value={p}>{p}</option>
            ))}
          </select>
        </div>
        <div className="form-field">
          <label htmlFor="assignee">Assignee (clear to unassign)</label>
          <input
            id="assignee"
            value={assignee}
            onChange={(e) => setAssignee(e.target.value)}
            maxLength={120}
          />
        </div>
        <button type="submit" className="btn" disabled={saving}>
          {saving ? "Saving…" : "Save changes"}
        </button>
      </form>

      <h3>Comments</h3>
      <ErrorAlert error={commentError} title="Could not add comment" />

      {ticket.comments.length === 0 ? (
        <p className="muted">No comments yet.</p>
      ) : (
        <ul className="comment-list card">
          {ticket.comments.map((c) => (
            <li key={c.id}>
              <strong>{c.author}</strong>
              <span className="muted"> · {formatDate(c.createdAt)}</span>
              <p>{c.body}</p>
            </li>
          ))}
        </ul>
      )}

      <form className="card" onSubmit={onAddComment}>
        <div className="form-field">
          <label htmlFor="author">Your name</label>
          <input
            id="author"
            value={commentAuthor}
            onChange={(e) => setCommentAuthor(e.target.value)}
            required
            maxLength={120}
          />
        </div>
        <div className="form-field">
          <label htmlFor="body">Comment</label>
          <textarea
            id="body"
            value={commentBody}
            onChange={(e) => setCommentBody(e.target.value)}
            required
            maxLength={2000}
          />
        </div>
        <button type="submit" className="btn" disabled={commentSubmitting}>
          {commentSubmitting ? "Posting…" : "Add comment"}
        </button>
      </form>
    </div>
  );
}
