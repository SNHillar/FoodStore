import type { ICategory } from "./category";

export interface Product {
    id: number;
    deleted: boolean;
    createdAt: string;
    name: string;
    price: number;
    description: string;
    stock: number;
    image: string;
    available: boolean;
    categoryDto: ICategory;
}


export interface CartItem extends Product {
    quantity: number;
}