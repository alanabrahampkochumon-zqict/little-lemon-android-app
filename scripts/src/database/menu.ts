import path from "path";
import sharp from "sharp";
import { fileURLToPath } from "url";
import menuData from "../../data/menu.json" with { type: "json" };
import supabase from "../supabase.js";
import { uploadImage } from "../utils/UploadImage.js";

export async function insertNutritionInfo() {
    console.log("Uploading nutrition info to supabase");
    const { error, data } = await supabase
        .from("nutrition_info")
        .upsert(menuData.nutrition_info)
        .select("id");
    if (error) {
        console.error(
            `There was an error inserting nutrition info: \n${error.message}`,
        );
    } else {
        console.log(
            `Nutrition Info inserted successfully. ${data.length} records inserted!`,
        );
    }
}

export async function insertCategories() {
    console.log("Uploading categories to supabase");
    const { error, data } = await supabase
        .from("category")
        .upsert(menuData.category)
        .select("id");

    if (error)
        console.error(
            `There was an error inserting categories info: \n${error.message}`,
        );
    else
        console.log(
            `Categories inserted successfully. ${data.length} records inserted!`,
        );
}

export async function insertDishes() {
    var dishesUploaded = 0;
    const dishes = [];

    console.log("Uploading images");
    for (let i = 0; i < menuData.dish.length; i++) {
        const imageName = menuData.dish[i].image;
        const { fullPath } = (await readAndUploadImage(imageName)) || {
            fullPath: `dishes/${imageName}`,
        };
        dishes.push({ ...menuData.dish[i], image: fullPath });
        dishesUploaded++;
        process.stdout.write(".");
    }
    console.log();
    console.log(dishesUploaded, "images uploaded!");

    // Insert the dishes
    await _insertDishToSupabase(dishes);
}

export async function insertDishCategories() {
    console.log("Uploading dish categories to supabase");
    const { error, data } = await supabase
        .from("dish_category")
        .upsert(menuData.dish_category)
        .select("dish_id");

    if (error)
        console.error(
            `There was an error inserting dish category: \n${error.message}`,
        );
    else
        console.log(
            `Dish categories inserted successfully. ${data.length} records inserted!`,
        );
}

/**
 * PRIVATE/HELPER FUNCTIONS
 */
const IMAGE_PATH = "../../images/dishes/";

function getAbsolutePath(imageName: string) {
    const __filename = fileURLToPath(import.meta.url);
    const __dirname = path.dirname(__filename);
    return path.join(__dirname, IMAGE_PATH, imageName);
}

async function _insertDishToSupabase(dishes: Dish[]) {
    console.log("Uploading dishes to supabase");
    const { error, data } = await supabase
        .from("dish")
        .upsert(dishes)
        .select("id");

    if (error)
        console.error(
            `There was an error inserting nutrition info.\n${error.message}`,
        );
    else
        console.log(
            `Dishes inserted successfully. ${data.length} records inserted!`,
        );
}

async function readAndUploadImage(imageName: string) {
    try {
        // Get the file name, and concat it with the the foldername's absolute path
        const fullPath = getAbsolutePath(imageName);
        // Read the image
        const image = await sharp(fullPath).toBuffer();

        const data = await uploadImage(
            process.env.SUPABASE_DISH_BUCKET || "",
            imageName,
            image,
            true,
        );
        return data;
    } catch (error: unknown) {
        if (error instanceof Error)
            console.error("Error uploading image:", error.message);
        else console.error("An unexpected error occurred:", error);
    }
}
