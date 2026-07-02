import type { CartItem, Product } from "../types/product";
import { showToast } from "../utils/toast";



export function getCart(): CartItem[] {
    return JSON.parse(localStorage.getItem("cart") || "[]") as CartItem[];
}

export function saveCart(cart: CartItem[]) {
    localStorage.setItem("cart", JSON.stringify(cart));
    updateCartCount();
}

export function addToCart(product: Product, quantity: number) {
    

    const cart = getCart();

    const existingItem = cart.find(item => item.id === product.id);

    if (existingItem) {
        existingItem.quantity += quantity;
    } else {
        cart.push({ ...product, quantity });
    }

    saveCart(cart);
    showToast("Producto agregado al carrito", "success");
}

export function removeFromCart(productId: number) {
    const cart = getCart();
    const existingItemIndex = cart.findIndex(item => item.id === productId);

    if (existingItemIndex !== -1) {
        cart[existingItemIndex].quantity--;
        if (cart[existingItemIndex].quantity <= 0) {
            cart.splice(existingItemIndex, 1);
        }
    }

    saveCart(cart);
    showToast("Producto eliminado del carrito", "success");
}

export function incrementQuantity(productId: number) {
    const cart = getCart();
    const existingItem = cart.find(item => item.id === productId);

    if (existingItem) {
        existingItem.quantity++;
    }

    saveCart(cart);
}

export function clearCart() {
    saveCart([]);
}

export function updateCartCount() {
    const cart = getCart();
    const totalCount = cart.reduce((acc, item) => acc + item.quantity, 0);
    const cartCountElement = document.getElementById("cart-count");
    if (cartCountElement) {
        cartCountElement.textContent = totalCount.toString();
        cartCountElement.style.display = totalCount > 0 ? "inline-block" : "none";
    }
}

