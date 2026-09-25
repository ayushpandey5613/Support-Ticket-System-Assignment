import type { Metadata } from "next";
import Link from "next/link";
import "./globals.css";

export const metadata: Metadata = {
  title: "Support Tickets",
  description: "Support Ticket Management System",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body>
        <header className="site-header">
          <div className="inner">
            <h1>
              <Link href="/">Support Tickets</Link>
            </h1>
            <Link href="/tickets/new" className="btn">
              New ticket
            </Link>
          </div>
        </header>
        <main className="container">{children}</main>
      </body>
    </html>
  );
}
