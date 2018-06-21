import { Component, OnInit } from '@angular/core';
import {AuthModel} from "../../models/auth.model";
import {AuthService} from "../../services/auth.service";
import {ActivatedRoute, Params, Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './page-login.component.html',
  styleUrls: ['./page-login.component.scss']
})
export class PageLoginComponent implements OnInit {

  errorMessage: String;
  errorList: String[];
  model: AuthModel;
  infoMessage: String;
  submitted: boolean;

  constructor(private activatedRoute: ActivatedRoute,
              private router: Router,
              private authService: AuthService) { }

  ngOnInit() {
    this.submitted = false;
    this.model = new AuthModel();
    this.errorMessage = "";
    this.errorList = [];
    this.handleInfoParam();
  }

  handleInfoParam() {
    const queryParams: Params = Object.assign({}, this.activatedRoute.snapshot.queryParams);
    this.infoMessage = queryParams.info ? queryParams.info : "";

    this.router.navigate([], {
      relativeTo: this.activatedRoute,
      queryParams: {
        ...this.activatedRoute.snapshot.queryParams,
        info: null,
      }
    });
  }

  onSubmit() {
    this.submitted = true;
    this.infoMessage = "";
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
