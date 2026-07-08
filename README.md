## Java Learning Projects

This repository contains multiple Java projects, each organized in its own **branch**.

> 💡 Switch branches to explore different projects:
>
> ```bash
> git branch -a        # list all branches
> git checkout <branch-name>   # switch to a project
> ```

---

---

# Feedback App — Backend API (`feedback-app` branch)

This is a fully functional backend service built for the [Frontend Mentor Product Feedback](https://www.frontendmentor.io/challenges/product-feedback-app-wbvUYqjR6) challenge. If you're building the frontend for that challenge and want a real backend to plug into, this is it.

## What it does

The core idea is **multi-tenant feedback boards**. Each frontend team (or company) creates their own **Organization**. Everything — feedback boards, items, comments, upvotes — lives inside that organization. Two different organizations never see each other's data.

Here's the full flow:

1. A user **registers** and **logs in** to get a JWT token.
2. They **create an Organization** (they become its owner).
3. They **invite other registered users** into their org.
4. Org members can **create feedback boards**, **post feedback items**, **upvote**, **comment**, and **reply** to comments.

---

## Tech Stack

- **Java 17 + Spring Boot**
- **PostgreSQL** (database)
- **Flyway** (database migrations — schema is versioned, no manual setup needed)
- **JWT** (authentication via Bearer tokens)

---

## Running Locally

### Prerequisites

- Java 17+
- PostgreSQL running locally (or via Docker)
- Maven

### Environment Variables

Set these before running:

| Variable      | Description                                                  |
| ------------- | ------------------------------------------------------------ |
| `JWT_SECRET`  | Secret key used to sign JWT tokens (any long random string)  |
| `DB_URL`      | JDBC URL e.g. `jdbc:postgresql://localhost:5432/feedback_db` |
| `DB_USERNAME` | Postgres username                                            |
| `DB_PASSWORD` | Postgres password                                            |

### Start the server

```bash
git checkout feedback-app
cd feedback-app
mvn spring-boot:run
```

The API runs on [https://kuy4p9c5mat9vfd03v347hkb.167.233.162.12.sslip.io/](https://kuy4p9c5mat9vfd03v347hkb.167.233.162.12.sslip.io/).

Hit the base URL in your browser and you'll see a landing page with links to the Swagger docs, this repo, and the original Frontend Mentor challenge.

Flyway will automatically create all tables on first run — no SQL scripts to run manually.

---

## Authentication

All protected endpoints require a **Bearer token** in the `Authorization` header:

```
Authorization: Bearer <your_jwt_token>
```

Tokens expire after **1 hour**.

Most endpoints also require an **Organization context** header so the server knows which org you're acting within:

```
X-Organization-Id: <your_organization_id>
```

---

## Testing with Swagger

Once the server is running, you can explore and test all endpoints interactively via the built-in Swagger UI:

```
https://kuy4p9c5mat9vfd03v347hkb.167.233.162.12.sslip.io/swagger-ui/index.html
```

The raw OpenAPI JSON spec is also available at:

```
https://kuy4p9c5mat9vfd03v347hkb.167.233.162.12.sslip.io/v3/api-docs
```

To test protected endpoints in Swagger:

1. Call `POST /api/auth/login` to get your token.
2. Click the **Authorize** button (top right of the Swagger page).
3. Enter your token as `Bearer <your_token>`.
4. Set `X-Organization-Id` in the relevant request headers before executing.

---

## API Reference

### Auth

#### Register

```
POST /api/auth/register
```

```json
{
  "name": "Jane Doe",
  "email": "jane@example.com",
  "password": "securepassword"
}
```

Response `201`:

```json
{
  "id": 1,
  "name": "Jane Doe",
  "email": "jane@example.com",
  "username": "jane@example.com",
  "joinedAt": "2024-01-01T12:00:00"
}
```

#### Login

```
POST /api/auth/login
```

```json
{
  "email": "jane@example.com",
  "password": "securepassword"
}
```

Response `200`:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "jane@example.com"
}
```

> Save the `token`. You'll need it for every subsequent request.

---

### Organizations

#### Create an Organization

```
POST /api/organization
```

```json
{
  "name": "Acme Corp Feedback",
  "memberId": 1
}
```

Response `201`:

```json
{
  "id": 1,
  "name": "Acme Corp Feedback",
  "createdAt": "2024-01-01T12:00:00"
}
```

> The user who creates the org becomes its **OWNER**. Save the `id` — this is your `X-Organization-Id`.

#### Invite a Member

```
POST /api/organization/invite
Authorization: Bearer <token>
X-Organization-Id: <org_id>
```

```json
{
  "memberId": 2
}
```

Response `201`:

```json
{
  "organizationId": 1,
  "memberId": 2,
  "memberName": "John Smith",
  "memberEmail": "john@example.com",
  "memberUsername": "john@example.com",
  "joinedAt": "2024-01-01T12:05:00",
  "role": "TESTER"
}
```

> Invited members join with the **TESTER** role. Roles: `OWNER`, `ADMIN`, `TESTER`.

#### Get Organization Members

```
GET /api/organization/{id}/members
Authorization: Bearer <token>
```

#### Get a Single Organization

```
GET /api/organization/{id}
```

#### Get All Organizations

```
GET /api/organization
```

#### Get Member's Organizations

```
GET /api/member/organizations
Authorization: Bearer <token>
```

---

### Feedback Boards

A **Feedback Board** is a collection of feedback items inside an organization. Think of it as a project or product area.

#### Create a Board

```
POST /api/feedback
Authorization: Bearer <token>
X-Organization-Id: <org_id>
```

```json
{
  "title": "Product Roadmap",
  "description": "Track feature requests and bug reports"
}
```

Response `201`:

```json
{
  "id": 1,
  "title": "Product Roadmap",
  "description": "Track feature requests and bug reports",
  "createdAt": "2024-01-01T12:00:00",
  "organizationId": 1
}
```

#### Get All Boards

```
GET /api/feedback
Authorization: Bearer <token>
X-Organization-Id: <org_id>
```

---

### Feedback Items

These are the actual feedback posts members submit.

#### Create a Feedback Item

```
POST /api/feedback/item/create
Authorization: Bearer <token>
X-Organization-Id: <org_id>
```

```json
{
  "title": "Add dark mode",
  "details": "The app should support a dark theme for night-time use.",
  "category": "FEATURE",
  "feedbackBoardId": 1,
  "state": "PLANNED"
}
```

**Category options:** `ALL`, `UI`, `UX`, `ENHANCEMENT`, `BUG`, `FEATURE`

**State options:** `PLANNED`, `IN_PROGRESS`, `COMPLETED`

Response `201`:

```json
{
  "id": 1,
  "title": "Add dark mode",
  "details": "The app should support a dark theme for night-time use.",
  "category": "FEATURE",
  "state": "PLANNED",
  "createdAt": "2024-01-01T12:00:00",
  "createdBy": { "id": 1, "name": "Jane Doe", ... },
  "upVotesCount": 0,
  "commentsCount": 0,
  "feedbackBoard": { "id": 1, "title": "Product Roadmap", ... }
}
```

#### Update a Feedback Item

```
PUT /api/feedback/item/{feedbackItemId}
Authorization: Bearer <token>
X-Organization-Id: <org_id>
```

```json
{
  "title": "Add dark mode",
  "details": "Updated description.",
  "category": "UI",
  "state": "IN_PROGRESS"
}
```

#### Get All Feedback Items

```
GET /api/feedback/items/all?page=1&size=10
Authorization: Bearer <token>
X-Organization-Id: <org_id>
```

**Query params:**

| Param  | Default | Description              |
| ------ | ------- | ------------------------ |
| `page` | `1`     | Page number (1-based)    |
| `size` | `10`    | Number of items per page |

Response `200`:

```json
{
  "content": [ ...feedback items... ],
  "page": {
    "number": 1,
    "size": 10,
    "totalElements": 42,
    "totalPages": 5
  }
}
```

#### Delete a Feedback Item

```
DELETE /api/feedback/item/{feedbackItemId}
Authorization: Bearer <token>
X-Organization-Id: <org_id>
```

Response: `204 No Content`

---

### Upvotes

Upvoting works as a **toggle**. Calling this endpoint adds an upvote if the member hasn't voted, or removes it if they have — like a checkbox.

```
POST /api/feedback/item/{feedbackItemId}/upvote
Authorization: Bearer <token>
X-Organization-Id: <org_id>
```

The response includes the updated `upVotesCount` on the item.

---

### Comments

#### Add a Comment

```
POST /api/feedback/item/{feedbackItemId}/comment
Authorization: Bearer <token>
X-Organization-Id: <org_id>
```

```json
{
  "body": "I'd love to see this! Dark mode would make late-night usage so much better."
}
```

Response `201`:

```json
{
  "id": 1,
  "body": "I'd love to see this!...",
  "author": { "id": 2, "name": "John Smith", ... }
}
```

#### Get Comments on a Feedback Item

```
GET /api/feedback/item/{feedbackItemId}/comments?page=1&size=10
Authorization: Bearer <token>
X-Organization-Id: <org_id>
```

**Query params:**

| Param  | Default | Description              |
| ------ | ------- | ------------------------ |
| `page` | `1`     | Page number (1-based)    |
| `size` | `10`    | Number of items per page |

Response `200`:

```json
{
  "content": [ ...comments... ],
  "page": {
    "number": 1,
    "size": 10,
    "totalElements": 24,
    "totalPages": 3
  }
}
```

---

### Replies (Sub-comments)

Comments support threaded replies. You can reply to a comment, and also reply to a reply (nested).

#### Add a Reply

```
POST /api/feedback/item/{feedbackItemId}/comment/{commentId}/subcomment
Authorization: Bearer <token>
X-Organization-Id: <org_id>
```

```json
{
  "body": "@Jane agreed, especially the dashboard.",
  "parentSubCommentId": null
}
```

> Set `parentSubCommentId` to the ID of another sub-comment if you're replying to a reply (nested threading). Leave it `null` for a direct reply to the top-level comment.

Response `201`:

```json
{
  "id": 1,
  "body": "@Jane agreed, especially the dashboard.",
  "replyTo": { "id": 1, "name": "Jane Doe", ... },
  "author": { "id": 2, "name": "John Smith", ... }
}
```

#### Get Replies on a Comment

```
GET /api/feedback/item/{feedbackItemId}/comment/{commentId}/subcomments?page=1&size=10
Authorization: Bearer <token>
X-Organization-Id: <org_id>
```

**Query params:**

| Param  | Default | Description              |
| ------ | ------- | ------------------------ |
| `page` | `1`     | Page number (1-based)    |
| `size` | `10`    | Number of items per page |

Response `200`:

```json
{
  "content": [ ...replies... ],
  "page": {
    "number": 1,
    "size": 10,
    "totalElements": 8,
    "totalPages": 1
  }
}
```

---

### Member Search

Search for registered users by name or email (useful for finding users to invite):

```
GET /api/member?searchQuery=jane&page=1&size=10
```

**Query params:**

| Param         | Default | Description                              |
| ------------- | ------- | ---------------------------------------- |
| `searchQuery` | —       | Optional. Filters by email prefix match. |
| `page`        | `1`     | Page number (1-based)                    |
| `size`        | `10`    | Number of items per page                 |

No auth required.

Response `200`:

```json
{
  "content": [ ...members... ],
  "page": {
    "number": 1,
    "size": 10,
    "totalElements": 3,
    "totalPages": 1
  }
}
```

---

## Integration Flow (Quick Start for Frontend Devs)

This API is **multi-tenant** — meaning every user gets their own private workspace. To make that work, there are two concepts not in the original Frontend Mentor design: **Organization** and **Feedback Board**.

Don't let that scare you. Here's all you need to know:

- **Organization** = your private workspace. You create it once, you get an ID back, you save that ID. Done.
- **Feedback Board** = a board inside your org where feedback items live. You create one, save its ID. Done.

After those two one-time steps, you never touch them again. Everything else — posting feedback, upvoting, commenting — is exactly what the Frontend Mentor design asks for.

```
─── Do this once ──────────────────────────────────────────────
1. POST /api/auth/register          → create your account
2. POST /api/auth/login             → get your JWT token (save it)
3. POST /api/organization           → create your org (save the org ID)
4. POST /api/feedback               → create a feedback board (save the board ID)

─── This is your app ──────────────────────────────────────────
5. POST /api/feedback/item/create         → post a feedback item
6. GET  /api/feedback/items/all           → fetch all items to display
7. POST /api/feedback/item/{id}/upvote    → toggle upvote
8. POST /api/feedback/item/{id}/comment   → add a comment
9. POST /api/organization/invite          → invite teammates into your org
```

Every frontend team gets their own isolated org. Your data never mixes with another team's.

---

## Database Schema Overview

```
member ──────────────────┐
                         │ (many-to-many via member_organization)
organization ────────────┘
     │
     └── feedback_board
              │
              └── feed_back_board_item
                       ├── up_vote (one per member per item)
                       └── comment
                                └── sub_comment (nested replies)
```

---

## Notes

- All timestamps are returned in ISO 8601 format.
- The database schema is managed by Flyway — migrations run automatically on startup. Never edit tables manually.
- JWT tokens are signed with HMAC-SHA. Keep your `JWT_SECRET` private.
- There is no password reset flow currently implemented.
