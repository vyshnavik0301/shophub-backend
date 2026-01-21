# ShopHub – All API Requests

Base URL: `http://localhost:8080`  
Use `Content-Type: application/json` for requests with a body.

---

## 1. AuthController — `/api/auth`

| Method | URL | Headers | Body |
|--------|-----|---------|------|
| **POST** | `/api/auth/register` | — | Yes |
| **POST** | `/api/auth/login` | — | Yes |
| **GET**  | `/api/auth/me`       | `X-User-Id` | No  |

### POST /api/auth/register
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "secret123"
}
```

### POST /api/auth/login
```json
{
  "email": "john@example.com",
  "password": "secret123"
}
```

### GET /api/auth/me
- **Header:** `X-User-Id: <userId>` (UUID from register/login)

---

## 2. UserController — `/api/users`

| Method | URL | Headers | Body |
|--------|-----|---------|------|
| **GET** | `/api/users/{id}` | — | No |

### GET /api/users/{id}
- **Path:** `{id}` = user UUID

---

## 3. ProductController — `/api/products`

| Method | URL | Headers | Body |
|--------|-----|---------|------|
| **GET**  | `/api/products`     | — | No  |
| **GET**  | `/api/products/{id}`| — | No  |
| **POST** | `/api/products`     | — | Yes |

### POST /api/products
```json
{
  "name": "Wireless Mouse",
  "description": "Ergonomic wireless mouse",
  "price": 29.99,
  "imageUrl": "https://example.com/mouse.jpg",
  "category": {
    "categoryId": "11111111-1111-1111-1111-111111111111"
  }
}
```
- **Required:** `name`, `price`, `category.categoryId` (must exist).  
- **Optional:** `description`, `imageUrl`.  
- Seed category ID from `data.sql`: `11111111-1111-1111-1111-111111111111`.

---

## 4. CatalogController — `/api/catalog`

| Method | URL | Headers | Body |
|--------|-----|---------|------|
| **GET** | `/api/catalog` | — | No |
| **GET** | `/api/catalog/search` | — | No |
| **GET** | `/api/catalog/filter` | — | No |
| **GET** | `/api/catalog/{productId}` | — | No |

### GET /api/catalog
- No query params.

### GET /api/catalog/search
- **Query (optional):** `query` — e.g. `/api/catalog/search?query=mouse`

### GET /api/catalog/filter
- **Query (all optional):** `category`, `minPrice`, `maxPrice`  
- Example: `/api/catalog/filter?category=Electronics&minPrice=10&maxPrice=100`

### GET /api/catalog/{productId}
- **Path:** `{productId}` = product UUID

---

## 5. CartController — `/api/users/{userId}/cart`

| Method | URL | Headers | Body |
|--------|-----|---------|------|
| **GET**    | `/api/users/{userId}/cart` | — | No  |
| **POST**   | `/api/users/{userId}/cart/items` | — | Yes |
| **PUT**    | `/api/users/{userId}/cart/items/{itemId}` | — | Yes |
| **DELETE** | `/api/users/{userId}/cart/items/{itemId}` | — | No  |

### GET /api/users/{userId}/cart
- **Path:** `{userId}` = user UUID

### POST /api/users/{userId}/cart/items
- **Path:** `{userId}` = user UUID  
- **Body:**
```json
{
  "productId": "existing-product-uuid",
  "quantity": 2
}
```

### PUT /api/users/{userId}/cart/items/{itemId}
- **Path:** `{userId}` = user UUID, `{itemId}` = cart item UUID  
- **Body:** `quantity` 0 = remove item.
```json
{
  "quantity": 3
}
```

### DELETE /api/users/{userId}/cart/items/{itemId}
- **Path:** `{userId}` = user UUID, `{itemId}` = cart item UUID

---

## 6. CheckoutController — `/api/users/{userId}`

| Method | URL | Headers | Body |
|--------|-----|---------|------|
| **POST** | `/api/users/{userId}/checkout` | — | Yes |

### POST /api/users/{userId}/checkout
- **Path:** `{userId}` = user UUID  
- **Body:**
```json
{
  "shippingAddress": "123 Main St, City, 12345",
  "contactPhone": "+1-555-123-4567",
  "contactEmail": "john@example.com",
  "paymentMethod": "CREDIT_CARD"
}
```
- **paymentMethod:** `CREDIT_CARD`, `DEBIT_CARD`, `PAYPAL`, `BANK_TRANSFER`, `CASH_ON_DELIVERY` (optional; can be null/empty).

---

## 7. PaymentController — `/api/payments`

| Method | URL | Headers | Body |
|--------|-----|---------|------|
| **POST** | `/api/payments/mock` | — | No (or `{}`) |

### POST /api/payments/mock
- No body required. Always returns `{"success": true, "message": "Payment successful"}`.

---

## 8. OrderController — `/api`

| Method | URL | Headers | Body |
|--------|-----|---------|------|
| **GET** | `/api/users/{userId}/orders` | — | No |
| **GET** | `/api/orders/{orderId}` | — | No |
| **GET** | `/api/orders/{orderId}/status` | — | No |

### GET /api/users/{userId}/orders
- **Path:** `{userId}` = user UUID

### GET /api/orders/{orderId}
- **Path:** `{orderId}` = order UUID

### GET /api/orders/{orderId}/status
- **Path:** `{orderId}` = order UUID

---

## Quick reference (method + path only)

| Method | Path |
|--------|------|
| POST | /api/auth/register |
| POST | /api/auth/login |
| GET | /api/auth/me |
| GET | /api/users/{id} |
| GET | /api/products |
| GET | /api/products/{id} |
| POST | /api/products |
| GET | /api/catalog |
| GET | /api/catalog/search |
| GET | /api/catalog/filter |
| GET | /api/catalog/{productId} |
| GET | /api/users/{userId}/cart |
| POST | /api/users/{userId}/cart/items |
| PUT | /api/users/{userId}/cart/items/{itemId} |
| DELETE | /api/users/{userId}/cart/items/{itemId} |
| POST | /api/users/{userId}/checkout |
| POST | /api/payments/mock |
| GET | /api/users/{userId}/orders |
| GET | /api/orders/{orderId} |
| GET | /api/orders/{orderId}/status |

---

## Suggested test order

1. **POST** /api/auth/register → get `userId`
2. **GET** /api/auth/me (header `X-User-Id: <userId>`)
3. **POST** /api/products (use categoryId `11111111-1111-1111-1111-111111111111` from data.sql) → get `productId`
4. **GET** /api/users/{userId}
5. **GET** /api/catalog, /api/catalog/search?query=..., /api/catalog/filter?minPrice=0
6. **GET** /api/catalog/{productId}
7. **POST** /api/users/{userId}/cart/items `{"productId":"<productId>","quantity":2}` → get `cartItemId`
8. **GET** /api/users/{userId}/cart
9. **PUT** /api/users/{userId}/cart/items/{itemId} `{"quantity":1}`
10. **POST** /api/users/{userId}/checkout (body above)
11. **POST** /api/payments/mock
12. **GET** /api/users/{userId}/orders → get `orderId`
13. **GET** /api/orders/{orderId}
14. **GET** /api/orders/{orderId}/status
15. **DELETE** /api/users/{userId}/cart/items/{itemId} (if you added another item and did not checkout)
