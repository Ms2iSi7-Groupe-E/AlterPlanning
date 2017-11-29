import {Component, OnInit} from '@angular/core';
import {AuthService} from "./services/auth.service";
import {AuthModel} from "./models/auth.model";
import {JwtModel} from "./models/jwt.model";
import {UserService} from "./services/user.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  private user = {};

  constructor(private authService: AuthService, private userService: UserService) { }

  ngOnInit(): void {

    const user = new AuthModel("admin@admin.fr", "admin");

    this.authService.auth(user).subscribe(
      res => {
        const token = res['data'];
        const jwt = new JwtModel(token);

        if (jwt.expireAt() < new Date()) {
          this.user = "token expiré";
          return;
        }

        AuthService.setToken(token);

        this.userService.getMe().subscribe(
          res => {
            this.user = res;
          },
          err => {
            console.error(err);
            this.user = err;
          }
        );
      },
      // Errors will call this callback instead:
      err => {
        console.error(err);
        this.user = err;
      }
    );
  }

}
