
# API Documentation

This document provides a detailed overview of the Posts API endpoints. The API allows for creating, reading, updating, and deleting posts, including support for images within posts.


---

## Endpoints

### Get Post by ID

`GET /api/v1/posts/{postId}`

Retrieve a single post by its ID.

- **Path Parameters:**
    - `postId` (integer, required) - The ID of the post to retrieve.

- **Responses:**
    - `200 OK` - Returns the requested post.

### Update Post

`PUT /api/v1/posts/{postId}`

Update an existing post by its ID.

- **Headers:**
    - `X-User-Id` (integer, required) - ID of the user performing the update.

- **Path Parameters:**
    - `postId` (integer, required) - The ID of the post to update.

- **Request Body:**
    - JSON object matching `PostRequest` schema.

- **Responses:**
    - `200 OK` - Returns the updated post.

### Delete Post

`DELETE /api/v1/posts/{postId}`

Delete a post by its ID.

- **Headers:**
    - `X-User-Id` (integer, required) - ID of the user performing the deletion.

- **Path Parameters:**
    - `postId` (integer, required) - The ID of the post to delete.

- **Responses:**
    - `200 OK` - Returns a confirmation string.

### Get User Posts

`GET /api/v1/posts`

Retrieve posts created by a specific user with optional pagination.

- **Headers:**
    - `X-User-Id` (integer, required) - ID of the user whose posts to retrieve.

- **Query Parameters:**
    - `page` (integer, optional, default: 0) - Page number.
    - `size` (integer, optional, default: 20) - Number of posts per page.

- **Responses:**
    - `200 OK` - Returns the list of user posts.

### Create Post

`POST /api/v1/posts`

Create a new post.

- **Headers:**
    - `X-User-Id` (integer, required) - ID of the user creating the post.

- **Request Body:**
    - JSON object matching `PostRequest` schema.

- **Responses:**
    - `200 OK` - Returns the created post.

---

## Schemas

### ImageRequest

```json
{
  "image_url": "string"
}
```

### PostRequest

```json
{
  "content": "string",
  "images": [ImageRequest]
}
```

### ImageResponse

```json
{
  "image_id": "integer",
  "image_url": "string"
}
```

### PostResponse

```json
{
  "post_id": "integer",
  "user_id": "integer",
  "content": "string",
  "created_at": "date-time string",
  "updated_at": "date-time string",
  "images": [ImageResponse]
}
```

### UserPostsResponse

```json
{
  "user_id": "integer",
  "posts": [PostResponse]
}
```