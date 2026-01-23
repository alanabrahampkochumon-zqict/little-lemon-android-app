import {
    insertCategories,
    insertDishCategories,
    insertDishes,
    insertNutritionInfo,
} from "./database/menu.js";

// Menu Data Insertion
await insertNutritionInfo();
await insertCategories();
await insertDishes();
await insertDishCategories();
