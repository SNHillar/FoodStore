import type { CartItem } from "../../../types/product";
import { getCart, removeFromCart, incrementQuantity, clearCart, updateCartCount } from "../../../services/cartService.ts";
import { createOrder } from "../../../services/orderService.ts";
import type { OrderDetail } from "../../../types/order.ts";
import { showToast } from "../../../utils/toast.ts";
import { getUSer } from "../../../utils/localStorage.ts";
import type { PaymentMethod } from "../../../types/order.ts";
import type { IUser } from "../../../types/IUser.ts";
import { logout, normalizeRole } from "../../../utils/auth.ts";


const cartContainer = document.getElementById("cart-items") as HTMLDivElement;
const cartTotal = document.getElementById("total-amount") as HTMLSpanElement;
const logoutButton = document.getElementById("logout-btn") as HTMLButtonElement;
const checkoutButton = document.getElementById("checkout-btn") as HTMLButtonElement;
const checkoutModal = document.getElementById("checkout-modal") as HTMLDivElement;
const closeCheckoutModalButton = document.querySelector(".checkout-modal__btn--close") as HTMLButtonElement;
const cancelCheckoutButton = document.querySelector(".checkout-form__btn--cancel") as HTMLButtonElement;
const adminNavLink = document.getElementById("admin-nav-link") as HTMLAnchorElement;


logoutButton.addEventListener("click", () => {
    logout();
});

const userData = getUSer();
if (adminNavLink && userData) {
    const user: IUser = JSON.parse(userData);
    adminNavLink.style.display = normalizeRole(user.role) === "admin" ? "block" : "none";
} else if (adminNavLink) {
    adminNavLink.style.display = "none";
}


function renderCart() {

    const cartItems: CartItem[] = getCart();

    if (cartItems.length === 0) {
        cartContainer.innerHTML = "<p>Tu carrito está vacío.</p>";
        cartTotal.textContent = "Total: $0";
        return;
    }

    cartContainer.innerHTML = "";

    let total: number = 0;
    cartItems.forEach((item: CartItem) => {
        const cartItemDiv = document.createElement("div") as HTMLDivElement;
        cartItemDiv.classList.add("cart-item");

        const itemName = document.createElement("h4") as HTMLHeadingElement;
        itemName.textContent = item.name;
        itemName.classList.add("cart-item__name");

        const itemsControlsDiv = document.createElement("div") as HTMLDivElement;
        itemsControlsDiv.classList.add("item__controls");

        const decreaseButton = document.createElement("button") as HTMLButtonElement;
        decreaseButton.textContent = "-";
        decreaseButton.classList.add("item-controls__btn", "decrease-btn");

        const quantitySpan = document.createElement("span") as HTMLSpanElement;
        quantitySpan.textContent = item.quantity.toString();
        quantitySpan.classList.add("item-controls__quantity");

        const increaseButton = document.createElement("button") as HTMLButtonElement;
        increaseButton.textContent = "+";
        increaseButton.classList.add("item-controls__btn", "increase-btn");

        const itemPrice = document.createElement("p") as HTMLParagraphElement;
        itemPrice.textContent = `Precio: $${item.price} x ${item.quantity}`;
        itemPrice.classList.add("cart-item__price");


        itemsControlsDiv.append(decreaseButton, quantitySpan, increaseButton);
        cartItemDiv.append(itemName, itemPrice, itemsControlsDiv);
        cartContainer.appendChild(cartItemDiv);
        total += item.price * item.quantity;
    });

    cartTotal.textContent = `Total: $${total}`;
    setupCartButtons(cartItems);
}



function setupCartButtons(cartItems: CartItem[]) {

    const decreaseButtons = document.querySelectorAll(".decrease-btn") as NodeListOf<HTMLButtonElement>;
    const increaseButtons = document.querySelectorAll(".increase-btn") as NodeListOf<HTMLButtonElement>;
    const clearCartButton = document.getElementById("clear-cart-btn") as HTMLButtonElement;

    decreaseButtons.forEach((button: HTMLButtonElement, index: number) => {
        button.onclick = () => {
            removeFromCart(cartItems[index].id);
            renderCart();
        };
    });

    increaseButtons.forEach((button: HTMLButtonElement, index: number) => {
        button.onclick = () => {
            incrementQuantity(cartItems[index].id);
            renderCart();
        };
    });

    if (clearCartButton) {
        clearCartButton.onclick = () => {
            clearCart();
            renderCart();
        };
    }
}

checkoutButton?.addEventListener("click", () => {
    if (getCart().length === 0) return;
    showCheckoutModal();
});

closeCheckoutModalButton?.addEventListener("click", hideCheckoutModal);
cancelCheckoutButton?.addEventListener("click", hideCheckoutModal);

function showCheckoutModal() {
    checkoutModal?.classList.add("checkout-modal--show");
}

function hideCheckoutModal() {
    checkoutModal?.classList.remove("checkout-modal--show");
}



function cartItemsToOrderDetails(cartItems: CartItem[]): OrderDetail[] {
    console.log(cartItems);
    return cartItems.map(item => ({
        productId: item.id,
        userId: JSON.parse(getUSer() as string).id as number,
        quantity: item.quantity,
    }));
}

const checkoutForm = document.getElementById("checkout-form") as HTMLFormElement;
checkoutForm?.addEventListener("submit", async (e: SubmitEvent) => {
    e.preventDefault();

    try {
        const formData = new FormData(checkoutForm);
        const paymentMethod = formData.get("paymentMethod")?.toString() as PaymentMethod;

        const userData = getUSer();
        if (!userData) {
            showToast("No se pudo obtener el usuario.", "error");
            return;
        }
        const user = JSON.parse(userData as string) as { id: number };
        const cartItems = getCart();

        if (cartItems.length === 0) {
            showToast("Tu carrito está vacío.", "error");
            return;
        }

        const items = cartItemsToOrderDetails(cartItems);
        const order = await createOrder(user.id, paymentMethod, items);
        console.log(order);
        clearCart();
        hideCheckoutModal();
        renderCart();
        showToast("Pedido creado con éxito.", "success");
    } catch (error) {
        console.error("Error al crear el pedido:", error);
        showToast("Hubo un error al crear el pedido.", "error");
    }
});


renderCart();
updateCartCount();

