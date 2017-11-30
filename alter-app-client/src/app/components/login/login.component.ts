import { Component, OnInit } from '@angular/core';
import {AuthModel} from "../../models/auth.model";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  error: String;
  model: AuthModel;
  submitted: Boolean;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit() {
    this.submitted = false;
    this.model = new AuthModel("", "");
    this.error = "";
  }

  onSubmit() {
    this.submitted = true;
    this.error = "";

    this.authService.login(this.model).subscribe(
      res => {
        const token = res["data"];
        AuthService.setToken(token);
        this.router.navigate(['/']);
      },
      err => {
        console.error(err);
        this.submitted = false;
        this.error = err['error']['message'];
      }
    )
  }

}
