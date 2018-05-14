import {JwtHelper} from "../helper/jwt.helper";

export class JwtModel {

  private parsedJwt: Object;

  constructor(private token: string) {
    this.parsedJwt = JwtHelper.decodeToken(token);
  }

  getUserUid(): String {
    return this.parsedJwt["sub"];
  }

  issuedAt(): Date {
    return new Date(this.parsedJwt["iat"] * 1000);
  }

  expireAt(): Date {
    return new Date(this.parsedJwt["exp"] * 1000);
  }

}
