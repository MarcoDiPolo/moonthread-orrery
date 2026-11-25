"""Generate Moonthread's original orbital launcher mark using Pillow."""
from pathlib import Path
from PIL import Image, ImageDraw, ImageFilter

root = Path(__file__).resolve().parents[1] / "app/src/main/res"
size = 1024
back = Image.new("RGBA", (size, size))
fore = Image.new("RGBA", (size, size))
d = ImageDraw.Draw(back)
d.rounded_rectangle((50,50,974,974), 225, fill="#211a32")
ink = ImageDraw.Draw(fore)
for r, y, colour in [(318,485,"#cfbfdd"),(248,500,"#76d8cc"),(178,510,"#f1a06a")]:
    ink.ellipse((512-r,y-r,512+r,y+r), outline=colour, width=13)
for x, y, colour in [(830,485,"#cfbfdd"),(512,748,"#76d8cc"),(334,510,"#f1a06a")]:
    ink.ellipse((x-24,y-24,x+24,y+24),fill=colour)
ink.ellipse((427,425,605,603),fill="#ffe4a4")
ink.ellipse((470,399,612,558),fill="#211a32")
ink.polygon([(512,107),(525,140),(552,153),(525,165),(512,199),(500,165),(473,153),(500,140)],fill="#ffe4a4")

combined=Image.alpha_composite(back,fore)
combined.save(root/'mipmap-xxxhdpi/ic_spatial_launcher.png',optimize=True)
fore.resize((1424,1424), Image.Resampling.LANCZOS).save(root/'drawable/ic_launcher_foreground.png',optimize=True)
for index, layer in enumerate([back,fore]):
    layer.save(root/f'drawable/icon_3d_layer_{index}.png',optimize=True)
    mask=layer.getchannel('A').filter(ImageFilter.GaussianBlur(4))
    Image.merge('RGBA',(mask,mask,mask,Image.new('L',mask.size,255))).save(root/f'drawable/icon_3d_sdf_{index}.png',optimize=True)
