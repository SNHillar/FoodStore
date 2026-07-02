import { CategoriaService } from "../../../services/categoryService";
import { getOrders } from "../../../services/orderService";
import { productService } from "../../../services/productService";
import { checkAuhtUser, logout } from "../../../utils/auth";

const categoriesCount = document.getElementById(
  "categories-count"
) as HTMLParagraphElement;

const productsCount = document.getElementById(
  "products-count"
) as HTMLParagraphElement;

const ordersCount = document.getElementById(
  "orders-count"
) as HTMLParagraphElement;

const productsActiveCount = document.getElementById(
  "products-active-count"
) as HTMLParagraphElement;

const summaryText = document.getElementById(
  "summary-text"
) as HTMLParagraphElement;


async function getCategoriesCount() {
  const categories = await CategoriaService.getAll();
  categoriesCount.textContent = categories.length.toString();
}

async function getProductsCount() {
  const products = await productService.getAll();
  productsCount.textContent = products.length.toString();
}

async function getOrdersCount() {
  const orders = await getOrders();
  ordersCount.textContent = orders.length.toString();
}

async function getProductsActiveCount() {
  const products = await productService.getAll();
  productsActiveCount.textContent = products.length.toString();
}

async function getSummaryText() {
  summaryText.textContent = "Cargando estadísticas...";
}

const buttonLogout = document.getElementById(
  "logout-btn"
) as HTMLButtonElement;
buttonLogout?.addEventListener("click", () => {
  logout();
});


const initPage = () => {
  console.log("inicio de pagina");
  checkAuhtUser(
    "/src/pages/auth/login/login.html",
    "/src/pages/store/home/home.html",
    "admin"
  );
  getCategoriesCount();
  getProductsCount();
  getOrdersCount();
  getProductsActiveCount();
  getSummaryText();
};

initPage();
