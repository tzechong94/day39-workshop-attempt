import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CharacterComponent } from './components/character/character.component';
import { CommentFormComponent } from './components/comment-form/comment-form.component';
import { ListComponent } from './components/list/list.component';
import { SearchComponent } from './components/search/search.component';

const routes: Routes = [
  { path:"", component: SearchComponent },
  { path:"list", component: ListComponent },
  { path:"characters/:characterId", component: CharacterComponent},
  { path: "characters/:characterId/comment", component: CommentFormComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
