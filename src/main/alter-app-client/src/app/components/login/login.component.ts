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

  errorMessage: String;
  errorList: String[];
  model: AuthModel;
  submitted: boolean;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit() {
    this.submitted = false;
    this.model = new AuthModel();
    this.errorMessage = "";
    this.errorList = [];
  }

  onSubmit() {
    this.submitted = true;
    this.errorMessage = "";
    this.errorList = [];

    this.authService.login(this.model).subscribe(
      res => {
        AuthService.setToken(res.token);
        this.router.navigate(['/']);
      },
      err => {
        console.error(err);
        const error = err.error;
        this.submitted = false;
        this.errorMessage = error.message;
        this.errorList = error.errorList ? error.errorList : [];
      }
    );
  }

}
