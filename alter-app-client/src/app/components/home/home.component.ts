import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  user = {};

  constructor(private authService: AuthService, private userService: UserService) { }

  ngOnInit() {
    this.userService.getMe().subscribe(
      res => {
        this.user = res['data'];
      },
      err => {
        console.error(err);
      }
    );
  }

  logout() {
    this.authService.logout();
  }
}
