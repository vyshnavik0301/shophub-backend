# Postman – Updated Test Steps (JWT Authentication)

**Base URL:** `http://localhost:8080`

## Important Changes
- ✅ User ID is now extracted from JWT token (Authorization header)
- ✅ Cart endpoints: `/api/cart` (no userId in path)
- ✅ Checkout endpoint: `/api/checkout` (no userId in path)
- ✅ Orders endpoint: `/api/orders` (no userId in path)
- ✅ All authenticated endpoints require `Authorization: Bearer <token>` header

---

## Quick Setup

1. **Import Collection:**
   - Import `ShopHub_API_Collection.postman_collection.json` into Postman
   - Collection variables are pre-configured

2. **Set Base URL:**
   - Collection variable `baseUrl` is set to `http://localhost:8080`
   - Modify if your server runs on a different port

---

## Test Flow

### Step 1: Register User
- **Method:** `POST`
- **URL:** `http://localhost:8080/api/auth/register`
- **Headers:** `Content-Type: application/json`
- **Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "secret123"
}
```
- **Response:** Save `userId` from response (auto-saved if using collection)

### Step 2: Login (Get JWT Token)
- **Method:** `POST`
- **URL:** `http://localhost:8080/api/auth/login`
- **Headers:** `Content-Type: application/json`
- **Body:**
```json
{
  "email": "john@example.com",
  "password": "secret123"
}
```
- **Response:** Copy `token` from response
- **Important:** This token is used in `Authorization: Bearer <token>` header for all authenticated requests

### Step 3: Get Current User
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/auth/me`
- **Headers:** 
  - `Authorization: Bearer <token-from-step-2>`
- **Response:** Returns current user info

---

## Cart Operations (Updated Routes)

### Step 4: Get Cart
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/cart`
- **Headers:** 
  - `Authorization: Bearer <token>`
- **Note:** User ID is automatically extracted from JWT token

### Step 5: Add Item to Cart
- **Method:** `POST`
- **URL:** `http://localhost:8080/api/cart/items`
- **Headers:** 
  - `Authorization: Bearer <token>`
  - `Content-Type: application/json`
- **Body:**
```json
{
  "productId": "<product-uuid>",
  "quantity": 2
}
```
- **Note:** Replace `<product-uuid>` with actual product ID from Step 6

### Step 6: Create Product (if needed)
- **Method:** `POST`
- **URL:** `http://localhost:8080/api/products`
- **Headers:** `Content-Type: application/json`
- **Body:**
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
- **Response:** Save `productId` from response

### Step 7: Update Cart Item
- **Method:** `PUT`
- **URL:** `http://localhost:8080/api/cart/items/<cartItemId>`
- **Headers:** 
  - `Authorization: Bearer <token>`
  - `Content-Type: application/json`
- **Body:**
```json
{
  "quantity": 3
}
```
- **Note:** Set quantity to `0` to remove item

### Step 8: Remove Cart Item
- **Method:** `DELETE`
- **URL:** `http://localhost:8080/api/cart/items/<cartItemId>`
- **Headers:** 
  - `Authorization: Bearer <token>`

---

## Checkout (Updated Route)

### Step 9: Checkout
- **Method:** `POST`
- **URL:** `http://localhost:8080/api/checkout`
- **Headers:** 
  - `Authorization: Bearer <token>`
  - `Content-Type: application/json`
- **Body:**
```json
{
  "shippingAddress": "123 Main St, City, 12345",
  "contactPhone": "+1-555-123-4567",
  "contactEmail": "john@example.com",
  "paymentMethod": "CREDIT_CARD"
}
```
- **Payment Methods:** `CREDIT_CARD`, `DEBIT_CARD`, `PAYPAL`, `BANK_TRANSFER`, `CASH_ON_DELIVERY`
- **Response:** Save `orderId` from response
- **Note:** User ID is automatically extracted from JWT token

---

## Orders (Updated Routes)

### Step 10: Get User Orders
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/orders`
- **Headers:** 
  - `Authorization: Bearer <token>`
- **Note:** Returns all orders for the authenticated user (user ID from JWT)

### Step 11: Get Order by ID
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/orders/<orderId>`
- **Headers:** 
  - `Authorization: Bearer <token>`

### Step 12: Get Order Status
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/orders/<orderId>/status`
- **Headers:** 
  - `Authorization: Bearer <token>`

---

## Complete Test Sequence

1. **POST** `/api/auth/register` → Get `userId`
2. **POST** `/api/auth/login` → Get `jwtToken` ⚠️ **Save this token!**
3. **GET** `/api/auth/me` (with `Authorization: Bearer <token>`)
4. **POST** `/api/products` → Get `productId`
5. **GET** `/api/catalog` (browse products)
6. **POST** `/api/cart/items` (with `Authorization: Bearer <token>`) → Get `cartItemId`
7. **GET** `/api/cart` (with `Authorization: Bearer <token>`)
8. **PUT** `/api/cart/items/<cartItemId>` (with `Authorization: Bearer <token>`)
9. **POST** `/api/checkout` (with `Authorization: Bearer <token>`) → Get `orderId`
10. **GET** `/api/orders` (with `Authorization: Bearer <token>`)
11. **GET** `/api/orders/<orderId>` (with `Authorization: Bearer <token>`)
12. **GET** `/api/orders/<orderId>/status` (with `Authorization: Bearer <token>`)

---

## Security Notes

✅ **User ID is now extracted from JWT token** - Users cannot access other users' resources by changing the URL

✅ **All authenticated endpoints require JWT token** in `Authorization: Bearer <token>` header

✅ **No userId in URL paths** for cart, checkout, and orders endpoints

---

## Troubleshooting

### 401 Unauthorized
- Check that JWT token is included in `Authorization: Bearer <token>` header
- Token may have expired - login again to get a new token

### 403 Forbidden
- User ID from JWT token doesn't match the resource owner
- This should not happen with the new implementation

### 404 Not Found
- Check that IDs (productId, cartItemId, orderId) are correct
- Ensure the resource exists

---

## Collection Variables

The Postman collection automatically manages these variables:
- `baseUrl`: `http://localhost:8080`
- `jwtToken`: Auto-filled after login
- `userId`: Auto-filled after register/login
- `productId`: Auto-filled after creating product
- `cartItemId`: Auto-filled after adding to cart
- `orderId`: Auto-filled after checkout
