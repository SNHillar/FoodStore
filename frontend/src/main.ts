import type { IUser } from "./types/IUser";
import { normalizeRole } from "./utils/auth";
import { navigate } from "./utils/navigate";

const userData: IUser | null = JSON.parse(localStorage.getItem("userData") || "null");
const path = window.location.pathname;


function cheackGuard(): void {
  
  const isAuthPage = path.includes("auth/login") || path.includes("/registro/");
  const isAdminPage = path.includes("/admin/");
  const isClientPage = path.includes("/client/");

  if(!userData || !userData.loggedIn) {
    if (!isAuthPage || isAdminPage || isClientPage) {
      navigate("/src/pages/auth/login/login.html");
    }
    return;
  }
    
  if (isAdminPage && normalizeRole(userData.role) !== "admin") {
    alert("No tienes permisos para acceder a esta página");
    navigate("/src/pages/store/home/home.html");
  }

  if (isClientPage && normalizeRole(userData.role) !== "user") {
    alert("No tienes permisos para acceder a esta página");
    navigate("/src/pages/admin/home/admin-home.html");
  }
};

cheackGuard();
