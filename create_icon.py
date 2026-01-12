from PIL import Image, ImageDraw

# Create a 16x16 white image
img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
draw = ImageDraw.Draw(img)

# Draw a simple eye/view icon (circle with dot)
draw.ellipse([2, 4, 14, 12], outline='white', width=1)
draw.ellipse([6, 7, 10, 11], fill='white')

# Save as PNG
img.save(r'src\main\resources\images\details.png')
print('Icon created successfully at src/main/resources/images/details.png')
