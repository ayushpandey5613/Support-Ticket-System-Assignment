"use client";

import { ErrorAlert } from "@/components/ErrorAlert";
import { createTicket } from "@/lib/api";
import { ApiError, Priority } from "@/lib/types";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { FormEvent, useState } from "react";

const PRIORITIES: Priority[] = ["LOW", "MEDIUM", "HIGH", "URGENT"];

export default function NewTicketPage() {
  const router = useRouter();
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [priority, setPriority] = useState<Priority>("MEDIUM");
  const [assignee, setAssignee] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<ApiError | Error | null>(null);
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});

  function mapFieldErrors(apiError: ApiError): Record<string, string> {
    const map: Record<string, string> = {};
    for (const fe of apiError.fieldErrors) {
      map[fe.field] = fe.message;
    }
    return map;
  }

  async function onSubmit(e: FormEvent) {
    e.preventDefault();
    setSubmitting(true);
    setError(null);
    setFieldErrors({});
    try {
      await createTicket({
        title,
        description,
        priority,
        assignee: assignee.trim() ? assignee.trim() : undefined,
      });
      router.push("/");
      router.refresh();
    } catch (err) {
      if (err instanceof ApiError) {
        setError(err);
        setFieldErrors(mapFieldErrors(err));
      } else {
        setError(err instanceof Error ? err : new Error("Create failed"));
      }
    } finally {
      setSubmitting(false);
    }
  }

  function fieldError(name: string): string | undefined {
    return fieldErrors[name];
  }

  return (
    <div>
      <p>
        <Link href="/">← Back to list</Link>
      </p>
      <h2>Create ticket</h2>

      <ErrorAlert error={error} title="Could not create ticket" />

      <form className="card" onSubmit={onSubmit} noValidate>
        <div className="form-field">
          <label htmlFor="title">Title</label>
          <input
            id="title"
            name="title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
            maxLength={200}
            aria-invalid={!!fieldError("title")}
          />
          {fieldError("title") && (
            <div className="inline-error">{fieldError("title")}</div>
          )}
        </div>

        <div className="form-field">
          <label htmlFor="description">Description</label>
          <textarea
            id="description"
            name="description"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            required
            maxLength={5000}
            aria-invalid={!!fieldError("description")}
          />
          {fieldError("description") && (
            <div className="inline-error">{fieldError("description")}</div>
          )}
        </div>

        <div className="form-field">
          <label htmlFor="priority">Priority</label>
          <select
            id="priority"
            name="priority"
            value={priority}
            onChange={(e) => setPriority(e.target.value as Priority)}
          >
            {PRIORITIES.map((p) => (
              <option key={p} value={p}>{p}</option>
            ))}
          </select>
          {fieldError("priority") && (
            <div className="inline-error">{fieldError("priority")}</div>
          )}
        </div>

        <div className="form-field">
          <label htmlFor="assignee">Assignee (optional)</label>
          <input
            id="assignee"
            name="assignee"
            value={assignee}
            onChange={(e) => setAssignee(e.target.value)}
            maxLength={120}
          />
          {fieldError("assignee") && (
            <div className="inline-error">{fieldError("assignee")}</div>
          )}
        </div>

        <button type="submit" className="btn" disabled={submitting}>
          {submitting ? "Creating…" : "Create ticket"}
        </button>
      </form>
    </div>
  );
}
