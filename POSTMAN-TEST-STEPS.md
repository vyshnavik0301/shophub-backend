# Postman – Steps to Test All ShopHub APIs

Base URL: **`http://localhost:8080`**

Before starting:
1. Run the app: `mvnw.cmd spring-boot:run`
2. In Postman, set **Body → raw → JSON** and **Content-Type: application/json** (or use the JSON type) for any request with a body.

---

## Optional: Postman variables

In **Environments** (or collection variables), define:

| Variable | Initial value        | Use for                         |
|----------|----------------------|---------------------------------|
| `baseUrl`| `http://localhost:8080` | Base for all requests        |
| `userId` | *(leave empty)*      | Filled after **Step 1**         |
| `productId` | *(leave empty)*   | Filled after **Step 3**         |
| `cartItemId` | *(leave empty)*  | Filled after **Step 7**         |
| `orderId` | *(leave empty)*    | Filled after **Step 10**        |

Then use `{{baseUrl}}`, `{{userId}}`, etc. in the URLs below. Or replace them manually with the IDs from the responses.

---

## Step 1 – Register user

- **Method:** `POST`
- **URL:** `http://localhost:8080/api/auth/register`  
  (or `{{baseUrl}}/api/auth/register`)
- **Headers:** — (ensure Body is JSON)
- **Body (raw, JSON):**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "secret123"
}
```

**From response:** copy `userId` into `{{userId}}` or a note.  
Expected: **201 Created** with a user object including `userId`.

---

## Step 2 – Login (mock)

- **Method:** `POST`
- **URL:** `http://localhost:8080/api/auth/login`
- **Body (raw, JSON):**
```json
{
  "email": "john@example.com",
  "password": "secret123"
}
```

**From response:** you can use the same `userId` as in Step 1.  
Expected: **200 OK** with user (no password in JSON).

---

## Step 3 – Get current user (mock: X-User-Id)

- **Method:** `GET`
- **URL:** `http://localhost:8080/api/auth/me`
- **Headers:**

| Key         | Value                          |
|-------------|--------------------------------|
| `X-User-Id` | *paste `userId` from Step 1*   |

**Body:** none.

Expected: **200 OK** with the same user.

---

## Step 4 – Get user by ID

- **Method:** `GET`
- **URL:** `http://localhost:8080/api/users/{{userId}}`  
  (replace `{{userId}}` with the real UUID)

**Body:** none.

Expected: **200 OK** with user.

---

## Step 5 – Create product

- **Method:** `POST`
- **URL:** `http://localhost:8080/api/products`
- **Body (raw, JSON):**
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

`11111111-1111-1111-1111-111111111111` is the seeded “Electronics” category from `data.sql`.  
**From response:** copy `productId` into `{{productId}}` or a note.  
Expected: **201 Created** with the created product.

---

## Step 6 – Get all products

- **Method:** `GET`
- **URL:** `http://localhost:8080/api/products`

**Body:** none.

Expected: **200 OK** with a JSON array of products.

---

## Step 7 – Get product by ID

- **Method:** `GET`
- **URL:** `http://localhost:8080/api/products/{{productId}}`

**Body:** none.

Expected: **200 OK** with one product.

---

## Step 8 – Catalog: get all

- **Method:** `GET`
- **URL:** `http://localhost:8080/api/catalog`

**Body:** none.

Expected: **200 OK** with a list of products (catalog view).

---

## Step 9 – Catalog: search

- **Method:** `GET`
- **URL:** `http://localhost:8080/api/catalog/search?query=mouse`

**Body:** none.

Expected: **200 OK** with products matching “mouse”. You can change `query` or omit it.

---

## Step 10 – Catalog: filter

- **Method:** `GET`
- **URL:** `http://localhost:8080/api/catalog/filter?category=Electronics&minPrice=0&maxPrice=100`

**Body:** none.

Expected: **200 OK** with filtered products. `category`, `minPrice`, `maxPrice` are all optional.

---

## Step 11 – Catalog: get by product ID

- **Method:** `GET`
- **URL:** `http://localhost:8080/api/catalog/{{productId}}`

**Body:** none.

Expected: **200 OK** with one product (including `availableStock` if inventory exists).

---

## Step 12 – Add item to cart

- **Method:** `POST`
- **URL:** `http://localhost:8080/api/users/{{userId}}/cart/items`
- **Body (raw, JSON):**
```json
{
  "productId": "PUT-PRODUCT-UUID-HERE",
  "quantity": 2
}
```

Replace `PUT-PRODUCT-UUID-HERE` with `{{productId}}` from Step 5.  
**From response:** copy `cartItemId` (or `cart_item_id` in the JSON) into `{{cartItemId}}`.  
Expected: **201 Created** with the cart item.

---

## Step 13 – Get cart

- **Method:** `GET`
- **URL:** `http://localhost:8080/api/users/{{userId}}/cart`

**Body:** none.

Expected: **200 OK** with cart (items, `totalAmount`). **404** if the user has no cart yet.

---

## Step 14 – Update cart item quantity

- **Method:** `PUT`
- **URL:** `http://localhost:8080/api/users/{{userId}}/cart/items/{{cartItemId}}`
- **Body (raw, JSON):**
```json
{
  "quantity": 3
}
```

Use `0` to remove the item (then you get **204 No Content**).  
Expected: **200 OK** with the updated cart item, or **204** when quantity is 0.

---

## Step 15 – Checkout

- **Method:** `POST`
- **URL:** `http://localhost:8080/api/users/{{userId}}/checkout`
- **Body (raw, JSON):**
```json
{
  "shippingAddress": "123 Main St, City, 12345",
  "contactPhone": "+1-555-123-4567",
  "contactEmail": "john@example.com",
  "paymentMethod": "CREDIT_CARD"
}
```

`paymentMethod` can be: `CREDIT_CARD`, `DEBIT_CARD`, `PAYPAL`, `BANK_TRANSFER`, `CASH_ON_DELIVERY`.  
**From response:** copy `orderId` into `{{orderId}}`.  
Expected: **201 Created** with the order. The cart is cleared.

---

## Step 16 – Payment mock

- **Method:** `POST`
- **URL:** `http://localhost:8080/api/payments/mock`

**Body:** none (or `{}`).

Expected: **200 OK** with `{"success":true,"message":"Payment successful"}`.

---

## Step 17 – Get user orders

- **Method:** `GET`
- **URL:** `http://localhost:8080/api/users/{{userId}}/orders`

**Body:** none.

Expected: **200 OK** with a list of orders. Use `orderId` from one of them for the next steps.

---

## Step 18 – Get order by ID

- **Method:** `GET`
- **URL:** `http://localhost:8080/api/orders/{{orderId}}`

**Body:** none.

Expected: **200 OK** with one order.

---

## Step 19 – Get order status

- **Method:** `GET`
- **URL:** `http://localhost:8080/api/orders/{{orderId}}/status`

**Body:** none.

Expected: **200 OK** with `{"status":"PENDING"}` (or another status).

---

## Step 20 – Remove cart item

- **Method:** `DELETE`
- **URL:** `http://localhost:8080/api/users/{{userId}}/cart/items/{{cartItemId}}`

**Body:** none.

Use this when the cart still has items (e.g. you added another item after Step 12 and did not checkout).  
Expected: **204 No Content**.

---

## Short checklist (in order)

| # | Method | URL | Body? |
|---|--------|-----|-------|
| 1 | POST | /api/auth/register | Yes |
| 2 | POST | /api/auth/login | Yes |
| 3 | GET | /api/auth/me | No (header: X-User-Id) |
| 4 | GET | /api/users/{{userId}} | No |
| 5 | POST | /api/products | Yes |
| 6 | GET | /api/products | No |
| 7 | GET | /api/products/{{productId}} | No |
| 8 | GET | /api/catalog | No |
| 9 | GET | /api/catalog/search?query=mouse | No |
| 10 | GET | /api/catalog/filter?category=Electronics&minPrice=0&maxPrice=100 | No |
| 11 | GET | /api/catalog/{{productId}} | No |
| 12 | POST | /api/users/{{userId}}/cart/items | Yes |
| 13 | GET | /api/users/{{userId}}/cart | No |
| 14 | PUT | /api/users/{{userId}}/cart/items/{{cartItemId}} | Yes |
| 15 | POST | /api/users/{{userId}}/checkout | Yes |
| 16 | POST | /api/payments/mock | No |
| 17 | GET | /api/users/{{userId}}/orders | No |
| 18 | GET | /api/orders/{{orderId}} | No |
| 19 | GET | /api/orders/{{orderId}}/status | No |
| 20 | DELETE | /api/users/{{userId}}/cart/items/{{cartItemId}} | No |

---

## IDs to copy

- After **Step 1:** `userId`
- After **Step 5:** `productId`
- After **Step 12:** `cartItemId` (field may be `cartItemId` or `cart_item_id` in JSON)
- After **Step 15:** `orderId`

If **POST /api/products** returns 400 or 500, ensure the app was started after `data.sql` has run so the “Electronics” category exists. Restart the app and try again.
