import { ApiError } from "@/lib/types";

interface ErrorAlertProps {
  error: ApiError | Error | null;
  title?: string;
}

export function ErrorAlert({ error, title = "Something went wrong" }: ErrorAlertProps) {
  if (!error) {
    return null;
  }

  const apiError = error instanceof ApiError ? error : null;
  const message = error.message;

  return (
    <div className="alert alert-error" role="alert">
      <strong>{title}</strong>
      <p>{message}</p>
      {apiError && apiError.fieldErrors.length > 0 && (
        <ul>
          {apiError.fieldErrors.map((fe) => (
            <li key={`${fe.field}-${fe.message}`}>
              <span className="field-name">{fe.field}</span>: {fe.message}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
