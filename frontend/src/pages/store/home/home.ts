import { CategoriaService } from "../../../services/categoryService.ts";
import { productService } from "../../../services/productService.ts";
import type { ICategory } from "../../../types/category.ts";
import type { Product } from "../../../types/product";
import { CATEGORY_ICONS } from "../../../utils/icons.ts";
import { addToCart, updateCartCount } from "../../../services/cartService.ts";
import { logout, normalizeRole } from "../../../utils/auth.ts";
import { getUSer } from "../../../utils/localStorage.ts";
import type { IUser } from "../../../types/IUser.ts";
// Seleccionamos los elementos del DOM que vamos a utilizar
const productsContainer = document.getElementById("productsGrid") as HTMLDivElement;
const logoutButton = document.getElementById("logout-btn") as HTMLButtonElement;
const adminNavLink = document.getElementById("admin-nav-link") as HTMLAnchorElement;


let categories: ICategory[] = [];
let products: Product[] = [];

// Si el botón de logout existe, le agregamos un evento para redirigir al login
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

async function loadProducts(){
    try{
        products = await productService.getAll();
        categories = await CategoriaService.getAll();
        renderProducts(products);
        renderCategories(categories);
    } catch(error){
        console.log("Error al cargar los prodcutos.", error)
    }
}



function renderProducts(products: Product[]) {

    // Si no hay contenedor, no hacemos nada
    if (!productsContainer) return;

    productsContainer.innerHTML = "";

    // Iteramos sobre los productos y creamos las tarjetas correspondientes
    products.forEach((product: Product) => {
        const productCard = document.createElement("div") as HTMLDivElement;
        productCard.classList.add("card");

        const productImage = document.createElement("img") as HTMLImageElement;
        productImage.classList.add("card__image");
        productImage.src = product.image;
        productImage.alt = product.name;

        const productCategory = document.createElement("p") as HTMLParagraphElement;
        productCategory.textContent = product.categoryDto.name;
        productCategory.classList.add("card__category");

        const productDescription = document.createElement("p") as HTMLParagraphElement;
        productDescription.textContent = product.description;
        productDescription.classList.add("card__description");

        const productName = document.createElement("h3") as HTMLHeadingElement;
        productName.textContent = product.name;
        productName.classList.add("card__name");

        const productPrice = document.createElement("p") as HTMLParagraphElement;
        productPrice.textContent = `$${product.price}`;
        productPrice.classList.add("card__price");

        const addToCartButton = document.createElement("button") as HTMLButtonElement;
        addToCartButton.textContent = "Agregar al carrito";
        addToCartButton.classList.add("card__btn");
        // Evento para agregar el producto al carrito
        addToCartButton.addEventListener("click", () => {

            addToCart(product, 1);

            addToCartButton.textContent = "Agregado";
            addToCartButton.style.backgroundColor = "var(--primary-dark)";

            setTimeout(() => {
                addToCartButton.textContent = "Agregar al carrito";
                addToCartButton.style.backgroundColor = "var(--accent-orange)";
            }, 1000);
        });


        const productDetailButton = document.createElement("button") as HTMLButtonElement;
        productDetailButton.textContent = "Ver detalles";
        productDetailButton.classList.add("card__btn", "card__btn--secondary");
        // Evento para redirigir a la página de detalles del producto
        productDetailButton.addEventListener("click", () => {
            window.location.href = `/src/pages/store/productDetail/productDetail.html?id=${product.id}`;
        }

        );


        // Agregamos los elementos a la tarjeta y luego la tarjeta al contenedor
        productCard.append(productCategory, productImage, productName, productDescription, productPrice, productDetailButton);
        productsContainer.appendChild(productCard);
    });

};

// Función para manejar la búsqueda de productos
const searchInput = document.getElementById("searchInput") as HTMLInputElement;

searchInput.addEventListener("input", () => {
    const searchTerm = searchInput.value.toLowerCase();
    const filteredProducts = products.filter((product: Product) =>
        product.name.toLowerCase().includes(searchTerm)
    );
    renderProducts(filteredProducts);
});

// Renderizado inicial de productos y categorías

function renderCategories(categories: ICategory[]) {
    const categoriasList = document.getElementById("categoriasList") as HTMLUListElement;


    if (!categoriasList) return;

    categoriasList.innerHTML = "";

    // Opción para mostrar todos los productos
    const allItem = document.createElement("li") as HTMLLIElement;

    allItem.innerHTML = `<span class="category-icon">${CATEGORY_ICONS["Todas"]}</span> Todas`;
    allItem.classList.add("sidebar__item");

    allItem.addEventListener("click", () => {
        setActiveCategory(allItem);
        renderProducts(products);
    });

    categoriasList.appendChild(allItem);

    // Iteramos sobre las categorías y creamos los elementos correspondientes
    categories.forEach((category: { name: string; id: number }) => {


        const svgIcon = CATEGORY_ICONS[category.name] || "";

        const categoryItem = document.createElement("li") as HTMLLIElement;
        categoryItem.innerHTML = `<span class="category-icon">${svgIcon}</span> ${category.name}`;
        categoryItem.classList.add("sidebar__item");
        categoriasList?.appendChild(categoryItem);

        // Evento para filtrar los productos por categoría al hacer clic en el nombre de la categoría
        categoryItem.addEventListener("click", () => {
            setActiveCategory(categoryItem);
            const filteredProducts = products.filter((product: Product) =>
                product.categoryDto?.id === category.id
            );

            renderProducts(filteredProducts);
        });
    });
}

function setActiveCategory(element: HTMLLIElement) {
    // Quita la clase active de todos los items
    document.querySelectorAll(".sidebar__item").forEach(item => {
        item.classList.remove("sidebar__item--active");
    });

    // Activa el seleccionado
    element.classList.add("sidebar__item--active");
}

// Renderizado inicial

document.addEventListener("DOMContentLoaded", () => {
    loadProducts();
})
updateCartCount();