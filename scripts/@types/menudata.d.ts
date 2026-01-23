/**
 * AI Generated: Model Gemini Pro 3
 */

interface NutritionInfo {
    id: string;
    calories: number;
    protein: number;
    carbs: number;
    fats: number;
}

interface Category {
    id: string;
    category_name: string;
}

interface Dish {
    id: string;
    title: string;
    description: string | undefined;
    price: number;
    discounted_price: number;
    stock: number;
    popularity_index: number;
    date_added: string;
    nutrition_info_id: string;
}

interface DishCategory {
    dish_id: string;
    category_id: string;
}

interface MenuData {
    nutrition_info: NutritionInfo[];
    category: Category[];
    dish: Dish[];
    dish_category: DishCategory[];
}
