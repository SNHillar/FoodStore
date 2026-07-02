import type { Order, OrderDetail, OrderStatus, PaymentMethod } from "../types/order";

const API_URL = "http://localhost:8080/api/orders";


type OrderApiResponse = Order & { orderDetails?: OrderDetail[] };

export async function getOrders(): Promise<Order[]> {
    const response = await fetch(`${API_URL}`);
    if (!response.ok) {
        throw new Error("Error al obtener los pedidos");
    }
    const orders = await response.json() as OrderApiResponse[];
    return orders.map(({ orderDetails, items, ...order }) => ({
        ...order,
        items: orderDetails ?? items ?? [],
    }));
}

export async function updateOrderStatus(orderId: number, status: OrderStatus): Promise<Order> {
    const response = await fetch(`${API_URL}/${orderId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ status }),
    });
    if (!response.ok) {
        throw new Error("Error al actualizar el estado del pedido");
    }
    return await response.json() as Order;
}

export async function getOrderById(orderId: number): Promise<Order> {
    const response = await fetch(`${API_URL}/${orderId}`);
    console.log(response);
    if (!response.ok) {
        throw new Error("Error al obtener el pedido");
    }
    return await response.json() as Order;
}

export async function createOrder(userId: number, paymentMethod: PaymentMethod, items: OrderDetail[]): Promise<Order> {
    const response = await fetch(`${API_URL}/user/${userId}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ paymentMethod, items }),
    });
    if (!response.ok) {
        throw new Error("Failed to create order");
    }
    return await response.json() as Order;
}

export async function deleteOrder(id: number) {
    const response = await fetch(`${API_URL}/${id}`, {
        method: 'DELETE'
    });
    if (response.status === 403 || response.status === 401) {
      throw new Error('Unauthorized access');
    }
    if(!response.ok){ throw new Error("Error to delete order.") }
    return response.json();
}
