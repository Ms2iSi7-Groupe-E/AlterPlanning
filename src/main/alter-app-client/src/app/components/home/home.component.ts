import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {PromotionService} from "../../services/promotion.service";
import {CourService} from "../../services/cour.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  user = {};
  promotions = [];
  selectedPromotion;
  cours = [];

  constructor(private userService: UserService,
              private promotionService: PromotionService) { }

  ngOnInit() {
    this.userService.getMe().subscribe(
      res => {
        this.user = res;
      },
      err => {
        console.error(err);
      }
    );

    this.promotionService.getPromotions().subscribe(
      res => {
        this.promotions = res;
      },
      err => {
        console.error(err);
      }
    );
  }

  changePromotion() {
    this.promotionService.getCourByCodePromotion(this.selectedPromotion.codePromotion).subscribe(
      res => {
        this.cours = res;
      },
      err => {
        console.error(err);
      }
    );
  }

}
