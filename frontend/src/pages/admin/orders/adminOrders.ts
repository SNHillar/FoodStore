import { getOrders, updateOrderStatus } from "../../../services/orderService";
import { userService } from "../../../services/userService";
import type { Order, OrderStatus } from "../../../types/order";
import { showToast } from "../../../utils/toast";

const ordersList = document.getElementById('orders-list');

const STATUS_CLASSES: Record<OrderStatus, string> = {
    PENDING:   'order-status--pending',
    CONFIRMED: 'order-status--confirmed',
    COMPLETED: 'order-status--completed',
    CANCELED:  'order-status--canceled',
};

function applyStatusClass(select: HTMLSelectElement, status: OrderStatus) {
    select.className = `order-status ${STATUS_CLASSES[status]}`;
}

async function loadOrders() {
    try {
        const orders = await getOrders();
        renderOrders(orders);
    } catch (error) {
        console.error('Error al cargar pedidos:', error);
        showToast('Error al cargar pedidos', 'error');
    }
}

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

        const orderDate = document.createElement('p') as HTMLParagraphElement;
        orderDate.classList.add('category-card__description');
        orderDate.textContent = `Fecha: ${new Date(order.date).toLocaleString()}`;

        const orderUser = document.createElement('p') as HTMLParagraphElement;
        orderUser.classList.add('category-card__description');
        orderUser.textContent = `Usuario: ${getUserName(order.userId)}`;
        getUserName(order.userId).then(name => {
            orderUser.textContent = `Usuario: ${name}`;
        });

        const orderStatus = document.createElement('select') as HTMLSelectElement;
        orderStatus.innerHTML = `
            <option value="PENDING">PENDING</option>
            <option value="CONFIRMED">CONFIRMED</option>
            <option value="COMPLETED">COMPLETED</option>
            <option value="CANCELED">CANCELED</option>
        `;
        orderStatus.value = order.status;
        applyStatusClass(orderStatus, order.status);

        orderStatus.addEventListener('change', async (e) => {
            if (!(e.target instanceof HTMLSelectElement)) return;
            const newStatus = e.target.value as OrderStatus;
            const previousStatus = order.status;
            try {
                applyStatusClass(orderStatus, newStatus);
                await updateOrderStatus(order.id, newStatus);
                order.status = newStatus;
                showToast('Estado actualizado', 'success');
                await loadOrders();
            } catch (error) {
                orderStatus.value = previousStatus;
                applyStatusClass(orderStatus, previousStatus);
                showToast('Error al actualizar el estado', 'error');
            }
        });

        const orderPaymentMethod = document.createElement('p') as HTMLParagraphElement;
        orderPaymentMethod.classList.add('category-card__description');
        orderPaymentMethod.textContent = `Método de pago: ${order.paymentMethod}`;

        const orderQuantity = document.createElement('p') as HTMLParagraphElement;
        orderQuantity.classList.add('category-card__description');
        orderQuantity.textContent = `Producto(s): ${totalQuantity}`;

        const orderTotal = document.createElement('p') as HTMLParagraphElement;
        orderTotal.classList.add('management-card__price');
        orderTotal.textContent = `Total: $${totalAmount.toLocaleString()}`;

        cardContent.append(orderId, orderDate, orderStatus, orderPaymentMethod, orderUser, orderQuantity, orderTotal);
        orderCard.appendChild(cardContent);
        ordersList.appendChild(orderCard);
    });
}

async function getUserName(userId: number): Promise<string> {
    try{
        return (await userService.getById(userId)).firstName + ' ' + (await userService.getById(userId)).lastName;
    } catch (error) {
        console.error('Error al obtener el nombre del usuario:', error);
        return 'Usuario desconocido';
    }   
}

loadOrders();