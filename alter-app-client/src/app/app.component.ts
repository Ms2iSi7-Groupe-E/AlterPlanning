import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {

    // const user = new AuthModel("admin@admin.fr", "admin");
    //
    // this.authService.login(user).subscribe(
    //   res => {
    //     const token = res['data'];
    //     const jwt = new JwtModel(token);
    //
    //     if (jwt.expireAt() < new Date()) {
    //       this.user = "token expiré";
    //       return;
    //     }
    //
    //     AuthService.setToken(token);
    //
    //     this.userService.getMe().subscribe(
    //       res => {
    //         this.user = res;
    //       },
    //       err => {
    //         console.error(err);
    //         this.user = err;
    //       }
    //     );
    //   },
    //   // Errors will call this callback instead:
    //   err => {
    //     console.error(err);
    //     this.user = err;
    //   }
    // );
  }

}
