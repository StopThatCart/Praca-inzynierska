import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddCustomWlasciwoscComponent } from './add-custom-wlasciwosc.component';

describe('AddCustomWlasciwoscComponent', () => {
  let component: AddCustomWlasciwoscComponent;
  let fixture: ComponentFixture<AddCustomWlasciwoscComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddCustomWlasciwoscComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddCustomWlasciwoscComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
