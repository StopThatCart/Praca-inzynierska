import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateRoslinaPageComponent } from './update-roslina-page.component';

describe('UpdateRoslinaPageComponent', () => {
  let component: UpdateRoslinaPageComponent;
  let fixture: ComponentFixture<UpdateRoslinaPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateRoslinaPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateRoslinaPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
