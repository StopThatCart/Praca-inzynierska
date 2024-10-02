import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { CommonModule } from '@angular/common';
import { PostListComponent } from './pages/post-list/post-list.component';
import { PostyPageComponent } from './pages/posty-page/posty-page.component';
import { PostResolverService } from './services/post-resolver-service.service';


const routes: Routes = [
  {
    path: '',
    data: { breadcrumb: 'Społeczność' },
   // component: RoslinaComponent,
    children: [
      {
        path: '',
        component: PostListComponent
      },
      {
        path: ':postId',
        component: PostyPageComponent,
        data: { breadcrumb: (data: any) =>`${data.post.tytul}` },
        resolve: { post: PostResolverService }
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PostRoutingModule { }
