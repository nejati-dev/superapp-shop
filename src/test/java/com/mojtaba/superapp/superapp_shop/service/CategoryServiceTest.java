package com.mojtaba.superapp.superapp_shop.service;

import com.mojtaba.superapp.superapp_shop.entity.Category;
import com.mojtaba.superapp.superapp_shop.exception.ResourceNotFoundException;
import com.mojtaba.superapp.superapp_shop.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository repo;

    @InjectMocks
    private CategoryServiceImpl service;

    private Category sample1;
    private Category sample2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sample1 = new Category();
        sample1.setCategoryId(1);
        sample2 = new Category();
        sample2.setCategoryId(2);
    }

    @Test
    @DisplayName("🟢 createCategory باید یک Category جدید را ذخیره کرده و برگرداند")
    void createCategory_shouldReturnSavedEntity() {
        when(repo.save(any(Category.class))).thenAnswer(invocation -> {
            Category arg = invocation.getArgument(0);
            arg.setCategoryId(10);
            return arg;
        });

        Category toSave = new Category();
        Category saved = service.createCategory(toSave);

        assertThat(saved.getCategoryId()).isEqualTo(10);
        verify(repo, times(1)).save(toSave);
    }

    @Test
    @DisplayName("🟢 getCategoryById وقتی وجود دارد Optional حاوی مقدار برمی‌گرداند")
    void getCategoryById_found() {
        when(repo.findById(1)).thenReturn(Optional.of(sample1));

        Optional<Category> result = service.getCategoryById(1);
        assertThat(result).isPresent();
        assertThat(result.get().getCategoryId()).isEqualTo(1);
        verify(repo, times(1)).findById(1);
    }

    @Test
    @DisplayName("🟢 getCategoryById وقتی وجود ندارد Optional خالی برمی‌گرداند")
    void getCategoryById_notFound() {
        when(repo.findById(5)).thenReturn(Optional.empty());

        Optional<Category> result = service.getCategoryById(5);
        assertThat(result).isNotPresent();
        verify(repo, times(1)).findById(5);
    }

    @Test
    @DisplayName("🟢 getAllCategories باید همه Categoryها را برگرداند")
    void getAllCategories() {
        when(repo.findAll()).thenReturn(Arrays.asList(sample1, sample2));

        List<Category> all = service.getAllCategories();
        assertThat(all).hasSize(2);
        assertThat(all).extracting("categoryId").containsExactlyInAnyOrder(1, 2);
        verify(repo, times(1)).findAll();
    }

    @Test
    @DisplayName("🟢 updateCategory وقتی Category وجود دارد باید آپدیت کرده و ذخیره کند")
    void updateCategory_found() {
        // موجود بودن آیتم اولیه
        when(repo.findById(1)).thenReturn(Optional.of(sample1));
        // ریپازیتر در save بر می‌گرداند همان نمونه به‌روزشده
        when(repo.save(any(Category.class))).thenAnswer(inv -> inv.getArgument(0));

        // تغییرات (مثلاً تغییر والد یا چیز دیگر)
        Category newDetails = new Category();
        newDetails.setParent(sample2); // فرضاً پدر را تغییر می‌دهیم

        Category updated = service.updateCategory(1, newDetails);

        assertThat(updated.getParent()).isEqualTo(sample2);
        verify(repo, times(1)).findById(1);
        verify(repo, times(1)).save(updated);
    }

    @Test
    @DisplayName("🔴 updateCategory وقتی Category وجود ندارد باید ResourceNotFoundException پرتاب کند")
    void updateCategory_notFound() {
        when(repo.findById(99)).thenReturn(Optional.empty());

        Category dummy = new Category();
        assertThatThrownBy(() -> service.updateCategory(99, dummy))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category not found");
        verify(repo, times(1)).findById(99);
        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("🟢 deleteCategory وقتی Category وجود دارد باید فراخوانی deleteById انجام شود")
    void deleteCategory_found() {
        when(repo.existsById(3)).thenReturn(true);
        doNothing().when(repo).deleteById(3);

        service.deleteCategory(3);

        verify(repo, times(1)).existsById(3);
        verify(repo, times(1)).deleteById(3);
    }

    @Test
    @DisplayName("🔴 deleteCategory وقتی Category وجود ندارد باید ResourceNotFoundException پرتاب کند")
    void deleteCategory_notFound() {
        when(repo.existsById(42)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteCategory(42))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category not found");
        verify(repo, times(1)).existsById(42);
        verify(repo, never()).deleteById(anyInt());
    }
}

