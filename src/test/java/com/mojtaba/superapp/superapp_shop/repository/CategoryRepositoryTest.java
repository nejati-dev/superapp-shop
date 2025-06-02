package com.mojtaba.superapp.superapp_shop.repository;

import com.mojtaba.superapp.superapp_shop.entity.Category;
import com.mojtaba.superapp.superapp_shop.entity.CategoryTranslation;
import com.mojtaba.superapp.superapp_shop.entity.CategoryTranslationId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryTranslationRepository translationRepository;

    // … (سایر تست‌ها)

    @Test
    @DisplayName("🟢 findByParentCategoryId والد را به‌درستی فیلتر می‌کند")
    void findByParentCategoryId() {
        // ۱) تعریف و ذخیره والد
        Category parent0 = new Category();
        Category parentSaved = categoryRepository.save(parent0);

        // ۲) ایجاد دو فرزند برای همان والد
        Category child1 = new Category();
        child1.setParent(parentSaved);
        Category child2 = new Category();
        child2.setParent(parentSaved);
        categoryRepository.save(child1);
        categoryRepository.save(child2);

        // ۳) یک Category دیگر بدون والد (برای اطمینان از فیلتر شدن درست)
        Category orphan = new Category();
        categoryRepository.save(orphan);

        // ۴) فراخوانی متد و بررسی نتیجه
        List<Category> children = categoryRepository.findByParentCategoryId(parentSaved.getCategoryId());
        assertThat(children).hasSize(2);

        // اینجا parentSaved فقط یک‌بار مقداردهی شده، بنابراین درون lambda قابل‌استفاده و صحیح است
        assertThat(children)
                .allMatch(cat -> parentSaved.getCategoryId().equals(cat.getParent().getCategoryId()));
    }

    @Test
    @DisplayName("🟢 ذخیره و بازیابی CategoryTranslation با کلید مرکب به‌درستی کار می‌کند")
    void saveAndFindTranslationByCategoryId() {
        // ایجاد یک Category و ذخیره
        Category cat = new Category();
        cat = categoryRepository.save(cat);

        // ایجاد یک ترجمه
        CategoryTranslationId tid = new CategoryTranslationId();
        tid.setCategoryId(cat.getCategoryId());
        tid.setLangCode("en");

        CategoryTranslation translation = new CategoryTranslation();
        translation.setId(tid);
        translation.setCategory(cat);
        translation.setName("Electronics");
        translation.setDescription("Electronic devices");

        translationRepository.save(translation);

        List<CategoryTranslation> list = translationRepository.findByIdCategoryId(cat.getCategoryId());
        assertThat(list).hasSize(1);
        CategoryTranslation found = list.get(0);
        assertThat(found.getId().getLangCode()).isEqualTo("en");
        assertThat(found.getName()).isEqualTo("Electronics");
    }

    @Test
    @DisplayName("🟢 وقتی یک Category بدون والد ذخیره می‌کنیم، باید قابل بازیابی با findById باشد")
    void saveAndFindCategoryById() {
        Category c = new Category();
        Category saved = categoryRepository.save(c);

        Optional<Category> found = categoryRepository.findById(saved.getCategoryId());
        assertThat(found).isPresent();
        assertThat(found.get().getCategoryId()).isEqualTo(saved.getCategoryId());
        assertThat(found.get().getParent()).isNull();
    }
}


