package com.littlelemon.application.menu.utils

import com.littlelemon.application.menu.data.local.dao.DishDao
import com.littlelemon.application.menu.data.local.models.CategoryEntity
import com.littlelemon.application.menu.data.local.models.DishCategoryCrossRef
import com.littlelemon.application.menu.data.local.models.DishEntity
import com.littlelemon.application.menu.data.local.models.DishWithCategories
import kotlinx.coroutines.flow.Flow

class FakeDishDao(
    private val seedDatabase: Boolean = true
) : DishDao {

    private val dishes = mutableListOf<DishEntity>()
    private val categories = mutableListOf<CategoryEntity>()
    private val dishCategoryCrossRefs = mutableListOf<DishCategoryCrossRef>()

    init {
        if (seedDatabase) {
            val generator = MenuEntityGenerators()
            val dishCount = 10
            val categoryCount = 10

            categories.addAll(generator.generateCategoryEntities(categoryCount))
            repeat(dishCount) {
                val dish = generator.generateDishEntity()
                dishes.add(dish)
                dishCategoryCrossRefs.addAll(
                    generator.generateDishCategoryCrossRef(
                        dish,
                        categories
                    )
                )
            }
        }
    }

    override suspend fun insertCategory(category: CategoryEntity): Long {
        categories.removeIf { it.categoryId == category.categoryId }
        categories.add(category)
        return 1
    }

    override suspend fun insertDish(dish: DishEntity): Long {
        dishes.removeIf { it.dishId == dish.dishId }
        dishes.add(dish)
        return 1

    }

    override suspend fun insertDishCategoryCrossRef(crossRef: DishCategoryCrossRef) {
        dishCategoryCrossRefs.removeIf { it.categoryId == crossRef.categoryId && it.dishId == crossRef.dishId }
        dishCategoryCrossRefs.add(crossRef)
    }

    override fun getAllDishes(limit: Int): Flow<List<DishWithCategories>> {
        TODO("Not yet implemented")
    }

    override fun getDishesSortedByPopularity(limit: Int): Flow<List<DishWithCategories>> {
        TODO("Not yet implemented")
    }

    override fun getDishesSortedByAdded(
        limit: Int,
        ascending: Boolean
    ): Flow<List<DishWithCategories>> {
        TODO("Not yet implemented")
    }

    override fun getDishesSortedByCalories(
        limit: Int,
        ascending: Boolean
    ): Flow<List<DishWithCategories>> {
        TODO("Not yet implemented")
    }

    override fun getDishesSortedByProtein(
        limit: Int,
        ascending: Boolean
    ): Flow<List<DishWithCategories>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllDishes() {
        TODO("Not yet implemented")
    }
}