import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Character } from 'src/app/model/models';
import { CharacterService } from 'src/app/service/character.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

form!: FormGroup
constructor(private fb: FormBuilder, private charSvc: CharacterService, private router: Router){}
characters: Character[] = []
sub$!: Subscription

search() {
  console.log("search")
  // this.charSvc.characterSearched = this.form.value.searchInput
  console.log("character searched", this.charSvc.characterSearched)
  this.charSvc.characterSearched = this.form.value.searchInput
  this.router.navigate(["list"])
}

createForm() {
  return this.fb.group({
    searchInput: this.fb.control<string>('', Validators.required)
  })
}

ngOnInit(): void {
    this.form = this.createForm()
}

}
