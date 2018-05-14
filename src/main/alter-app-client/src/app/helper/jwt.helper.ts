import {JwtModel} from "../models/jwt.model";

export class JwtHelper {

  public static decodeToken(token: string): JwtModel {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace('-', '+').replace('_', '/');
    return JSON.parse(window.atob(base64));
  }

}
