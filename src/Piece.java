public class Piece {
	//arr[7][4]: 7 shapes, 4 blocks each shape
	public Location[][] arr = {
		{new Location(1, 0), new Location(0, 0), new Location(2, 0), new Location(3, 0)},
		{new Location(1, 1), new Location(0, 1), new Location(0, 0), new Location(2, 1)},
		{new Location(1, 1), new Location(0, 1), new Location(2, 1), new Location(2, 0)},
		{new Location(0, 0), new Location(1, 0), new Location(0, 1), new Location(1, 1)},
		{new Location(1, 0), new Location(0, 0), new Location(1, 1), new Location(2, 1)},
		{new Location(1, 1), new Location(0, 1), new Location(1, 0), new Location(2, 0)},
		{new Location(1, 1), new Location(1, 0), new Location(0, 1), new Location(2, 1)}
	};

	public Location[] toPrint = {new Location(0, 0), new Location(0, 0), new Location(0, 0), new Location(0, 0)};

	public void setShape(int id)
	{
		for (int i = 0 ; i < 4 ; i ++)
		{
			toPrint[i].x = arr[id][i].x;
			toPrint[i].y = arr[id][i].y;
		}
	}

	public void rotate(int dir)
	{
		for (int i = 1 ; i < 4 ; i ++)
		{
			int newVecX = (1 *  dir * (toPrint[i].y - toPrint[0].y));
			int newVecY = (-1 * dir * (toPrint[i].x - toPrint[0].x));
			toPrint[i].x = newVecX + toPrint[0].x;
			toPrint[i].y = newVecY + toPrint[0].y;
		}
	}
}
