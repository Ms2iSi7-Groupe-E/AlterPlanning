import {Component, OnInit} from '@angular/core';
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {HandleIndependantModuleComponent} from "../modal/handle-independant-module/handle-independant-module.component";
import {ActionTypes} from "../../models/enums/action-types";

@Component({
  selector: 'app-page-independant-modules',
  templateUrl: './page-independant-modules.component.html',
  styleUrls: ['./page-independant-modules.component.scss']
})
export class PageIndependantModulesComponent implements OnInit {

  constructor(private modalService: NgbModal) { }

  ngOnInit() {
  }

  openCreateModal() {
    const modalRef = this.modalService.open(HandleIndependantModuleComponent, { size: 'lg' });
    modalRef.componentInstance.action = ActionTypes.CREATE;
    modalRef.componentInstance.save.subscribe(res => {
      console.log(res);
    });
  }

}
