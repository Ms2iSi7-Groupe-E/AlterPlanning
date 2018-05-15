import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  user = {};

  constructor(private userService: UserService) { }

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


}
