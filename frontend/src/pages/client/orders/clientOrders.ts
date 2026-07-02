import { getOrders } from "../../../services/orderService";
import type { Order, OrderStatus } from "../../../types/order";
import { getCurrentUser, logout } from "../../../utils/auth";
import { showToast } from "../../../utils/toast";


const logoutBtn = document.getElementById('logout-btn') as HTMLButtonElement;
const userId = getCurrentUser()?.id;


logoutBtn.addEventListener('click', () => {
    logout();
});


const ordersList = document.getElementById('orders-list') as HTMLDivElement;
const STATUS_CLASSES: Record<OrderStatus, string> = {
    PENDING:   'order-status--pending',
    CONFIRMED: 'order-status--confirmed',
    COMPLETED: 'order-status--completed',
    CANCELED:  'order-status--canceled',
};

function renderOrders(orders: Order[]) {
    if (!ordersList) return;

    ordersList.innerHTML = '';

    orders.forEach(order => {
        const totalQuantity = order.items.reduce((acc, item) => acc + item.quantity, 0);
        const totalAmount = order.items.reduce((acc, item) => acc + (item.subtotal ?? 0), 0);

        const orderCard = document.createElement('article') as HTMLElement;
        orderCard.classList.add('management-card');

        const cardContent = document.createElement('div') as HTMLDivElement;
        cardContent.classList.add('management-card__content', 'management-card__content--categories');

        const orderId = document.createElement('h3') as HTMLHeadingElement;
        orderId.classList.add('category-card__title');
        orderId.textContent = `Pedido #ORDER-000${order.id}`;

        const orderStatus = document.createElement('p') as HTMLParagraphElement;
        orderStatus.classList.add('category-card__description');
        orderStatus.textContent = `Estado: ${order.status}`;
        orderStatus.className = `order-status ${STATUS_CLASSES[order.status as OrderStatus]}`;

        const orderDate = document.createElement('p') as HTMLParagraphElement;
        orderDate.classList.add('category-card__description');
        orderDate.textContent = `Fecha: ${new Date(order.date).toLocaleString()}`;

        const orderPaymentMethod = document.createElement('p') as HTMLParagraphElement;
        orderPaymentMethod.classList.add('category-card__description');
        orderPaymentMethod.textContent = `Método de pago: ${order.paymentMethod}`;

        const orderQuantity = document.createElement('p') as HTMLParagraphElement;
        orderQuantity.classList.add('category-card__description');
        orderQuantity.textContent = `Producto(s): ${totalQuantity}`;

        const orderTotal = document.createElement('p') as HTMLParagraphElement;
        orderTotal.classList.add('management-card__price');
        orderTotal.textContent = `Total: $${totalAmount.toLocaleString()}`;

        cardContent.append(orderId, orderStatus, orderDate, orderPaymentMethod, orderQuantity, orderTotal);
        orderCard.appendChild(cardContent);
        ordersList.appendChild(orderCard);
    });
}
const getOrdersByUserId = async (userId: number) => {
    const orders = await getOrders();
    return orders.filter(order => order.userId === userId);
}


document.addEventListener('DOMContentLoaded', () => {
    getOrdersByUserId(userId as number).then(orders => {
        renderOrders(orders);
    }).catch(error => {
        console.error('Error al cargar los pedidos:', error);
        showToast('Error al cargar los pedidos', 'error');
    });

    setInterval(() => {
        getOrdersByUserId(userId as number).then(orders => {
            renderOrders(orders);
        }).catch(error => {
            console.error('Error al cargar los pedidos:', error);
            showToast('Error al cargar los pedidos', 'error');
        });
    }, 5000);
});
