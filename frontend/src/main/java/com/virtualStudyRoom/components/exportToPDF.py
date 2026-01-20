from PIL import Image
from reportlab.pdfgen import canvas as pdf_canvas
from reportlab.lib.pagesizes import A4
import sys, os

def export_whiteboard_to_pdf(image_path, pdf_path):
    img = Image.open(image_path)
    img_width, img_height = img.size
    page_width, page_height = A4
    c = pdf_canvas.Canvas(pdf_path, pagesize=A4)
    scale = page_width / img_width
    scaled_width = page_width
    scaled_height = img_height * scale
    y = 0
    while y < scaled_height:
        remaining_height = min(page_height, scaled_height - y)
        crop_top = int(y / scale)
        crop_bottom = int((y + remaining_height) / scale)
        img_crop = img.crop((0, crop_top, img_width, crop_bottom))
        temp_path = "_temp_crop.png"
        img_crop.save(temp_path)
        c.drawImage(temp_path, 0, page_height - remaining_height, width=scaled_width, height=remaining_height)
        c.showPage()
        y += remaining_height
    c.save()
    if os.path.exists(temp_path):
        os.remove(temp_path)

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python exportToPDF.py <input_image.png> <output.pdf>")
        sys.exit(1)

    input_png = sys.argv[1]   
    output_pdf = sys.argv[2]  

    export_whiteboard_to_pdf(input_png, output_pdf)
