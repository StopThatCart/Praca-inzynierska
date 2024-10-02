import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UstawieniaPageComponent } from './ustawienia-page.component';

describe('UstawieniaPageComponent', () => {
  let component: UstawieniaPageComponent;
  let fixture: ComponentFixture<UstawieniaPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UstawieniaPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UstawieniaPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
