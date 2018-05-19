import { Component, OnInit } from '@angular/core';
import {AuthModel} from "../../models/auth.model";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './page-login.component.html',
  styleUrls: ['./page-login.component.scss']
})
export class PageLoginComponent implements OnInit {

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
        this.router.navigate(['/']);
      },
      err => {
        const error = err.error;
        this.submitted = false;
        this.errorMessage = error.message;
        this.errorList = error.errorList ? error.errorList : [];
      }
    );
  }

}