import menuData from "../data/menu.json" with { type: "json" };
import supabase from "../supabase.js";

export async function insertNutritionInfo() {
    const { error, data } = await supabase
        .from("nutrition_info")
        .upsert(menuData.nutrition_info)
        .select("id");
    if (error) {
        console.error(
            `There was an error inserting nutrition info.\n${error.message}`,
        );
    } else {
        console.log(
            `Nutrition Info inserted successfully. ${data.length} records inserted!`,
        );
    }
}

export async function insertCategories() {
    const { error, data } = await supabase
        .from("category")
        .upsert(menuData.category)
        .select("id");

    if (error)
        console.error(
            `There was an error inserting nutrition info.\n${error.message}`,
        );
    else
        console.log(
            `Categories inserted successfully. ${data.length} records inserted!`,
        );
}
