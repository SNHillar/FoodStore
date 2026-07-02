import type { Rol } from "./Rol";

export interface IUser {
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  password: string;
  loggedIn: boolean;
  role: Rol;
}
