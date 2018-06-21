import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  user;

  constructor(private authService: AuthService, private userService: UserService) { }

  ngOnInit() {
    this.userService.getMe().subscribe(
      res => {
        this.user = res;
      },
      err => {
        console.error(err);
      }
    );
  }

  logout() {
    this.authService.logout("Vous avez été déconnecté avec succès");
  }
}
