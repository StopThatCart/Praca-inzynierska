import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ZmianaHaslaEmailComponent } from './zmiana-hasla-email.component';

describe('ZmianaHaslaEmailComponent', () => {
  let component: ZmianaHaslaEmailComponent;
  let fixture: ComponentFixture<ZmianaHaslaEmailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ZmianaHaslaEmailComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ZmianaHaslaEmailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
