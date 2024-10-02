import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PowiadomieniaPageComponent } from './powiadomienia-page.component';

describe('PowiadomieniaPageComponent', () => {
  let component: PowiadomieniaPageComponent;
  let fixture: ComponentFixture<PowiadomieniaPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PowiadomieniaPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PowiadomieniaPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
