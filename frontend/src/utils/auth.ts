import type { IUser } from "../types/IUser";
import type { Rol } from "../types/Rol";
import { getUSer, removeUser } from "./localStorage";
import { navigate } from "./navigate";

export const normalizeRole = (role: string): "admin" | "user" => {
  const normalized = role.toLowerCase();
  return normalized === "admin" ? "admin" : "user";
};

/**
 * Protege una página según el rol requerido.
 * - redireccion1: si no hay sesión (login)
 * - redireccion2: si el rol no coincide (home del otro tipo de usuario)
 */
export const checkAuhtUser = (redireccion1: string, redireccion2: string, rol: Rol) => {
  const user = getUSer();

  if (!user) {
    navigate(redireccion1);
    return;
  }

  const parseUser: IUser = JSON.parse(user);
  if (normalizeRole(parseUser.role) !== normalizeRole(rol)) {
    navigate(redireccion2);
  }
};

/** Redirige al panel admin o al catálogo según el rol del usuario logueado. */
export const redirectAfterLogin = (user: IUser) => {
  if (normalizeRole(user.role) === "admin") {
    navigate("/src/pages/admin/home/admin-home.html");
  } else {
    navigate("/src/pages/store/home/home.html");
  }
};

export const getCurrentUser = () => {
  const user = getUSer();
  return user ? JSON.parse(user) : null;
};

export const logout = () => {
  removeUser();
  navigate("/src/pages/auth/login/login.html");
};
